/*
 * Copyright (c) 2007 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package atomic;

import java.util.AbstractList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * It is a thread safe and lock-free vector.
 * This class implement algorithm from:<br>
 *
 * Lock-free Dynamically Resizable Arrays <br>
 *
 * Damian Dechev, Peter Pirkelbauer, and Bjarne Stroustrup<br>
 * Texas A&M University College Station, TX 77843-3112<br>
 * {dechev, peter.pirkelbauer}@tamu.edu, bs@cs.tamu.edu
 *
 *
 * @author Zhi Gan
 *
 * @param <E> type of element in the vector
 *
 */
public class LockFreeVector<E> extends AbstractList<E> {

	private static final boolean debug = false;
	/**
	 * Size of the first bucket. sizeof(bucket[i+1])=2*sizeof(bucket[i])
	 */
	private static final int FIRST_BUCKET_SIZE = 8;

	/**
	 * number of buckets. 30 will allow 8*(2^30-1) elements
	 * 元素固定 30个篮子
	 */
	private static final int N_BUCKET = 30;

	/**
	 * We will have at most N_BUCKET number of buckets. And we have
	 * sizeof(buckets.get(i))=FIRST_BUCKET_SIZE**(i+1)
	 * 对象数组   AtomicReferenceArray  二维数组  非均衡二位数组   多线程修改一个变量
	 */
	private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;

	/**
	 * @author ganzhi
	 * 辅助工具类   写工具类
	 * @param <E>
	 */
	static class WriteDescriptor<E> {
		public E oldV;
		public E newV;
		public AtomicReferenceArray<E> addr;   //要修改的原子数组
		public int addr_ind;  // 位置

		/**
		 * Creating a new descriptor.
		 *
		 * @param addr Operation address
		 * @param addr_ind	Index of address
		 * @param oldV old operand
		 * @param newV new operand
		 */
		public WriteDescriptor(AtomicReferenceArray<E> addr, int addr_ind,
							   E oldV, E newV) {
			this.addr = addr;
			this.addr_ind = addr_ind;
			this.oldV = oldV;
			this.newV = newV;
		}

		/**
		 * set newV.
		 */
		public void doIt() {
			addr.compareAndSet(addr_ind, oldV, newV);
		}
	}

	/**
	 * @author ganzhi
	 *使用CAS操作写入的数据
	 * @param <E>
	 */
	static class Descriptor<E> {
		public int size;   //整个Vector的长度,什么意思？有多少个E意思就是
		volatile WriteDescriptor<E> writeop;  //写入

		/**
		 * Create a new descriptor.
		 *
		 * @param size Size of the vector
		 * @param writeop Executor write operation
		 */
		public Descriptor(int size, WriteDescriptor<E> writeop) {
			this.size = size;
			this.writeop = writeop;
		}

		/**
		 * 真正写入数组
		 */
		public void completeWrite() {
			WriteDescriptor<E> tmpOp = writeop;
			if (tmpOp != null) {
				tmpOp.doIt();
				writeop = null; // this is safe since all write to writeop use
				// null as r_value.
			}
		}
	}

	//
	private AtomicReference<Descriptor<E>> descriptor;  //CAS操作必备
	private static final int zeroNumFirst = Integer
			.numberOfLeadingZeros(FIRST_BUCKET_SIZE);  //28

	/**
	 * Constructor.
	 */
	public LockFreeVector() {
		buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(N_BUCKET);
		//0位置设置  引用数组
		buckets.set(0, new AtomicReferenceArray<E>(FIRST_BUCKET_SIZE));
		//初始化的值Descriptor为0和null
		descriptor = new AtomicReference<Descriptor<E>>(new Descriptor<E>(0,null));
	}

	/**
	 * add e at the end of vector.
	 *
	 * @param e
	 *            element added
	 *            关键计算哪个桶和哪个桶对应的下标
	 *            push_back(1);
	 */
	public void push_back(E e) {
		Descriptor<E> desc;
		Descriptor<E> newd;
		do {
			desc = descriptor.get();  //Descriptor
			desc.completeWrite();
			//  1 + 8  通过pos计算桶下标
			int pos = desc.size + FIRST_BUCKET_SIZE; // 1 + 8
			//  前导0数目
			int zeroNumPos = Integer.numberOfLeadingZeros(pos);
			int bucketInd = zeroNumFirst - zeroNumPos; //落入数组
			// 扩容
			if (buckets.get(bucketInd) == null) {
				int newLen = 2 * buckets.get(bucketInd - 1).length();
				if (debug)
					System.out.println("New Length is:" + newLen);
				buckets.compareAndSet(bucketInd, null,
						new AtomicReferenceArray<E>(newLen));
			}
			//  落入数组的下标
			int idx = (0x80000000>>>zeroNumPos) ^ pos;
			//真正写入的值
			newd = new Descriptor<E>(desc.size + 1, new WriteDescriptor<E>(
					buckets.get(bucketInd), idx, null, e));

		} while (!descriptor.compareAndSet(desc, newd));
		descriptor.get().completeWrite();
	}

	/**
	 * Remove the last element in the vector.
	 *
	 * @return element removed
	 */
	public E pop_back() {
		Descriptor<E> desc;
		Descriptor<E> newd;
		E elem;
		do {
			desc = descriptor.get();
			desc.completeWrite();

			int pos = desc.size + FIRST_BUCKET_SIZE - 1;
			int bucketInd = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
					- Integer.numberOfLeadingZeros(pos);
			int idx = Integer.highestOneBit(pos) ^ pos;
			elem = buckets.get(bucketInd).get(idx);
			newd = new Descriptor<E>(desc.size - 1, null);
		} while (!descriptor.compareAndSet(desc, newd));

		return elem;
	}

	/**
	 * Get element with the index.
	 *
	 * @param index
	 *            index
	 * @return element with the index
	 */
	@Override
	public E get(int index) {
		int pos = index + FIRST_BUCKET_SIZE;
		int zeroNumPos = Integer.numberOfLeadingZeros(pos);
		int bucketInd = zeroNumFirst - zeroNumPos;
		int idx = (0x80000000>>>zeroNumPos) ^ pos;
		return buckets.get(bucketInd).get(idx);
	}

	/**
	 * Set the element with index to e.
	 *
	 * @param index
	 *            index of element to be reset
	 * @param e
	 *            element to set
	 */
	/**
	 * {@inheritDoc}
	 */
	public E set(int index, E e) {
		int pos = index + FIRST_BUCKET_SIZE;
		int bucketInd = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
				- Integer.numberOfLeadingZeros(pos);
		int idx = Integer.highestOneBit(pos) ^ pos;
		AtomicReferenceArray<E> bucket = buckets.get(bucketInd);
		while (true) {
			E oldV = bucket.get(idx);
			if (bucket.compareAndSet(idx, oldV, e))
				return oldV;
		}
	}

	/**
	 * reserve more space.
	 *
	 * @param newSize
	 *            new size be reserved
	 */
	public void reserve(int newSize) {
		int size = descriptor.get().size;
		int pos = size + FIRST_BUCKET_SIZE - 1;
		int i = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
				- Integer.numberOfLeadingZeros(pos);
		if (i < 1)
			i = 1;

		int initialSize = buckets.get(i - 1).length();
		while (i < Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
				- Integer.numberOfLeadingZeros(newSize + FIRST_BUCKET_SIZE - 1)) {
			i++;
			initialSize *= FIRST_BUCKET_SIZE;
			buckets.compareAndSet(i, null, new AtomicReferenceArray<E>(
					initialSize));
		}
	}

	/**
	 * size of vector.
	 *
	 * @return size of vector
	 */
	public int size() {
		return descriptor.get().size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E object) {
		push_back(object);
		return true;
	}
}
