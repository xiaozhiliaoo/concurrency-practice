/**
 * @packgeName: PACKAGE_NAME
 * @ClassName: CPJ
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/9-1:21
 * @version: 1.0
 * @since: JDK 1.8
 */

/*
  Sample code file for CPJ2e.

  All code has been pasted directly from the camera-ready copy, and
  then modified in the smallest possible way to ensure that it will
  compile -- adding import statements or full package qualifiers for
  some class names, adding stand-ins for classes and methods that are
  referred to but not listed, and supplying dummy arguments instead of
  "...").

  They are presented in page-number order.

*/


import EDU.oswego.cs.dl.util.concurrent.*;
import java.util.*;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.beans.*;
import java.net.*;

class Helper {  // Dummy standin for referenced generic "Helper" classes
    void handle() {}
    void operation() {}
}



class Particle {
    protected int x;
    protected int y;
    protected final Random rng = new Random();

    public Particle(int initialX, int initialY) {
        x = initialX;
        y = initialY;
    }

    public synchronized void move() {
        x += rng.nextInt(10) - 5;
        y += rng.nextInt(20) - 10;
    }

    public void draw(Graphics g) {
        int lx, ly;
        synchronized (this) { lx = x; ly = y; }
        g.drawRect(lx, ly, 10, 10);
    }
}

class ParticleCanvas extends Canvas {

    private Particle[] particles = new Particle[0];

    ParticleCanvas(int size) {
        setSize(new Dimension(size, size));
    }

    // Intended to be called by applet
    synchronized void setParticles(Particle[] ps) {
        if (ps == null)
            throw new IllegalArgumentException("Cannot set null");

        particles = ps;
    }

    protected synchronized Particle[] getParticles() {
        return particles;
    }

    public void paint(Graphics g) { // override Canvas.paint
        Particle[] ps = getParticles();

        for (int i = 0; i < ps.length; ++i)
            ps[i].draw(g);

    }

}

class ParticleApplet extends Applet {

    protected Thread[] threads; // null when not running

    protected final ParticleCanvas canvas
            = new ParticleCanvas(100);

    public void init() { add(canvas); }

    protected Thread makeThread(final Particle p) { // utility
        Runnable runloop = new Runnable() {
            public void run() {
                try {
                    for(;;) {
                        p.move();
                        canvas.repaint();
                        Thread.sleep(100); // 100msec is arbitrary
                    }
                }
                catch (InterruptedException e) { return; }
            }
        };
        return new Thread(runloop);
    }

    public synchronized void start() {
        int n = 10; // just for demo

        if (threads == null) { // bypass if already started
            Particle[] particles = new Particle[n];
            for (int i = 0; i < n; ++i)
                particles[i] = new Particle(50, 50);
            canvas.setParticles(particles);

            threads = new Thread[n];
            for (int i = 0; i < n; ++i) {
                threads[i] = makeThread(particles[i]);
                threads[i].start();
            }
        }
    }

    public synchronized void stop() {
        if (threads != null) { // bypass if already stopped
            for (int i = 0; i < threads.length; ++i)
                threads[i].interrupt();
            threads = null;
        }
    }
}

class AssertionError extends java.lang.Error {
    public AssertionError() { super(); }
    public AssertionError(String message) { super(message); }
}


interface Tank {
    float getCapacity();
    float getVolume();
    void  transferWater(float amount)
            throws OverflowException, UnderflowException;
}

class OverflowException extends Exception {}
class UnderflowException extends Exception {}


class TankImpl {
    public float getCapacity() { return 1.0f; }
    public float getVolume() { return 1.0f; }
    public void  transferWater(float amount)
            throws OverflowException, UnderflowException {}
}


class Performer { public void perform() {} }

class AdaptedPerformer implements Runnable {
    private final Performer adaptee;

    public AdaptedPerformer(Performer p) { adaptee = p; }
    public void run() { adaptee.perform(); }
}

class AdaptedTank implements Tank {
    protected final Tank delegate;

    public AdaptedTank(Tank t) { delegate = t; }

    public float getCapacity() { return delegate.getCapacity(); }

    public float getVolume() { return delegate.getVolume(); }

    protected void checkVolumeInvariant() throws AssertionError {
        float v = getVolume();
        float c = getCapacity();
        if ( !(v >= 0.0 && v <= c) )
            throw new AssertionError();
    }

    public synchronized void transferWater(float amount)
            throws OverflowException, UnderflowException {


        checkVolumeInvariant();  // before-check

        try {
            delegate.transferWater(amount);
        }

        // postpone rethrows until after-check
        catch (OverflowException ex)  { throw ex; }
        catch (UnderflowException ex) { throw ex; }

        finally {
            checkVolumeInvariant(); // after-check
        }
    }

}


abstract class AbstractTank implements Tank {
    protected void checkVolumeInvariant() throws AssertionError {
        // ... identical to AdaptedTank version ...
    }

    protected abstract void doTransferWater(float amount)
            throws OverflowException, UnderflowException;

    public synchronized void transferWater(float amount)
            throws OverflowException, UnderflowException {
        // identical to AdaptedTank version except for inner call:

        // ...
        try {
            doTransferWater(amount);
        }
        finally {}
        // ...
    }
}

class ConcreteTank extends AbstractTank {
    protected final float capacity = 10.f;
    protected float volume;
    // ...
    public float getVolume() { return volume; }
    public float getCapacity() { return capacity; }

    protected void doTransferWater(float amount)
            throws OverflowException, UnderflowException {
        // ... implementation code ...
    }
}
interface TankOp {
    void op() throws OverflowException, UnderflowException;
}
class TankWithMethodAdapter {
    // ...
    protected void checkVolumeInvariant() throws AssertionError {
        // ... identical to AdaptedTank version ...
    }

    protected void runWithinBeforeAfterChecks(TankOp cmd)
            throws OverflowException, UnderflowException {
        // identical to AdaptedTank.transferWater
        //   except for inner call:

        // ...
        try {
            cmd.op();
        }
        finally {}

        // ...
    }

    protected void doTransferWater(float amount)
            throws OverflowException, UnderflowException {
        // ... implementation code ...
    }

    public synchronized void transferWater(final float amount)
            throws OverflowException, UnderflowException {

        runWithinBeforeAfterChecks(new TankOp() {
            public void op()
                    throws OverflowException, UnderflowException {
                doTransferWater(amount);
            }
        });
    }
}


class StatelessAdder {
    public int add(int a, int b) { return a + b; }
}

class ImmutableAdder {
    private final int offset;

    public ImmutableAdder(int a) { offset = a; }

    public int addOffset(int b) { return offset + b; }
}

class Fraction {                             // Fragments
    protected final long numerator;
    protected final long denominator;

    public Fraction(long num, long den) {
        // normalize:
        boolean sameSign = (num >= 0) == (den >= 0);
        long n = (num >= 0)? num : -num;
        long d = (den >= 0)? den : -den;
        long g = gcd(n, d);
        numerator = (sameSign)? n / g : -n / g;
        denominator = d / g;
    }

    static long gcd(long a, long b) {
        // ... compute greatest common divisor ...
        return 1;
    }

    public Fraction plus(Fraction f) {
        return new Fraction(numerator * f.denominator +
                f.numerator * denominator,
                denominator * f.denominator);
    }

    public boolean equals(Object other) { // override default
        if (! (other instanceof Fraction) ) return false;
        Fraction f = (Fraction)(other);
        return numerator * f.denominator ==
                denominator * f.numerator;
    }

    public int hashCode() {              // override default
        return (int) (numerator ^ denominator);
    }
}

class Server { void doIt() {} }

class Relay {
    protected final Server server;

    Relay(Server s) { server = s; }

    void doIt() { server.doIt(); }
}

class Even {                                    //  Do not use
    private int n = 0;
    public int next(){ // POST?: next is always even
        ++n;
        ++n;
        return n;
    }
}

class ExpandableArray {

    protected Object[] data;  // the elements
    protected int size = 0;   // the number of array slots used
    // INV: 0 <= size <= data.length

    public ExpandableArray(int cap) {
        data = new Object[cap];
    }

    public synchronized int size() {
        return size;
    }

    public synchronized Object get(int i) // subscripted access
            throws NoSuchElementException {
        if (i < 0 || i >= size )
            throw new NoSuchElementException();

        return data[i];
    }

    public synchronized void add(Object x) { // add at end
        if (size == data.length) { // need a bigger array
            Object[] olddata = data;
            data = new Object[3 * (size + 1) / 2];
            System.arraycopy(olddata, 0, data, 0, olddata.length);
        }
        data[size++] = x;
    }

    public synchronized void removeLast()
            throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        data[--size] = null;
    }
}

interface Procedure {
    void apply(Object obj);
}

class ExpandableArrayWithApply extends ExpandableArray {

    public ExpandableArrayWithApply(int cap) { super(cap); }

    synchronized void applyToAll(Procedure p) {
        for (int i = 0; i < size; ++i)
            p.apply(data[i]);
    }
}

class ExpandableArrayWithIterator extends ExpandableArray {
    protected int version = 0;

    public ExpandableArrayWithIterator(int cap) { super(cap); }

    public synchronized void removeLast()
            throws NoSuchElementException {
        super.removeLast();
        ++version;               // advertise update
    }

    public synchronized void add(Object x) {
        super.add(x);
        ++version;
    }

    public synchronized Iterator iterator() {
        return new EAIterator();
    }

    protected class  EAIterator implements Iterator {
        protected final int currentVersion;
        protected int currentIndex = 0;

        EAIterator() { currentVersion = version; }

        public Object next() {
            synchronized(ExpandableArrayWithIterator.this) {
                if (currentVersion != version)
                    throw new ConcurrentModificationException();
                else if (currentIndex == size)
                    throw new NoSuchElementException();
                else
                    return data[currentIndex++];
            }
        }

        public boolean hasNext() {
            synchronized(ExpandableArrayWithIterator.this) {
                return (currentIndex < size);
            }
        }

        public void remove() {
            // similar
        }
    }
}

class LazySingletonCounter {
    private final long initial;
    private long count;

    private LazySingletonCounter() {
        initial = Math.abs(new java.util.Random().nextLong() / 2);
        count = initial;
    }

    private static LazySingletonCounter s = null;

    private static final Object classLock =
            LazySingletonCounter.class;

    public static LazySingletonCounter instance() {
        synchronized(classLock) {
            if (s == null)
                s = new LazySingletonCounter();
            return s;
        }
    }

    public long next() {
        synchronized(classLock) { return count++; }
    }

    public void reset() {
        synchronized(classLock) { count = initial; }
    }
}


class EagerSingletonCounter {
    private final long initial;
    private long count;

    private EagerSingletonCounter() {
        initial = Math.abs(new java.util.Random().nextLong() / 2);
        count = initial;
    }

    private static final EagerSingletonCounter s =
            new EagerSingletonCounter();

    public static EagerSingletonCounter instance() { return s; }
    public synchronized long next() { return count++; }
    public synchronized void reset() { count = initial; }
}

class StaticCounter {
    private static final long initial =
            Math.abs(new java.util.Random().nextLong() / 2);
    private static long count = initial;
    private StaticCounter() { } // disable instance construction
    public static synchronized long next() { return count++; }
    public static synchronized void reset() { count = initial; }
}


class Cell {                                    // Do not use
    private long value;
    synchronized long getValue() { return value; }
    synchronized void setValue(long v) { value = v; }

    synchronized void swapValue(Cell other) {
        long t = getValue();
        long v = other.getValue();
        setValue(v);
        other.setValue(t);
    }
}


class Cell2 {                                    // Do not use
    private long value;
    synchronized long getValue() { return value; }
    synchronized void setValue(long v) { value = v; }

    public void swapValue(Cell2 other) {
        if (other == this) // alias check
            return;
        else if (System.identityHashCode(this) <
                System.identityHashCode(other))
            this.doSwapValue(other);
        else
            other.doSwapValue(this);
    }

    protected synchronized void doSwapValue(Cell2 other) {
        // same as original public version:
        long t = getValue();
        long v = other.getValue();
        setValue(v);
        other.setValue(t);
    }

    protected synchronized void doSwapValueV2(Cell2 other) {
        synchronized(other) {
            long t = value;
            value = other.value;
            other.value = t;
        }
    }
}

final class SetCheck {
    private int  a = 0;
    private long b = 0;

    void set() {
        a =  1;
        b = -1;
    }

    boolean check() {
        return ((b ==  0) ||
                (b == -1 && a == 1));
    }
}

final class VFloat {
    private float value;

    final synchronized void  set(float f) { value = f; }
    final synchronized float get()        { return value; }
}

class Plotter {                                  // fragments
    // ...

    public void showNextPoint() {
        Point p = new Point();
        p.x = computeX();
        p.y = computeY();
        display(p);
    }

    int computeX() { return 1; }
    int computeY() { return 1; }

    protected void display(Point p) {
        // somehow arrange to show p.
    }
}


class SessionBasedService {                     // Fragments
    // ...
    public void service() {
        OutputStream output = null;
        try {
            output = new FileOutputStream("...");
            doService(output);
        }
        catch (IOException e) {
            handleIOFailure();
        }
        finally {
            try { if (output != null) output.close(); }
            catch (IOException ignore) {} // ignore exception in close
        }
    }

    void handleIOFailure() {}

    void doService(OutputStream s) throws IOException {
        s.write(0);
        // ... possibly more handoffs ...
    }
}


class ThreadPerSessionBasedService { // fragments
    // ...
    public void service() {
        Runnable r = new Runnable() {
            public void run() {
                OutputStream output = null;
                try {
                    output = new FileOutputStream("...");
                    doService(output);
                }
                catch (IOException e) {
                    handleIOFailure();
                }
                finally {
                    try { if (output != null) output.close(); }
                    catch (IOException ignore) {}
                }
            }
        };
        new Thread(r).start();
    }

    void handleIOFailure() {}




    void doService(OutputStream s) throws IOException {
        s.write(0);
        // ... possibly more hand-offs ...
    }
}

class ThreadWithOutputStream extends Thread {
    private OutputStream output;

    ThreadWithOutputStream(Runnable r, OutputStream s) {
        super(r);
        output = s;
    }

    static ThreadWithOutputStream current()
            throws ClassCastException {
        return (ThreadWithOutputStream) (currentThread());
    }

    static OutputStream getOutput() { return current().output; }

    static void setOutput(OutputStream s) { current().output = s;}
}


class ServiceUsingThreadWithOutputStream {        // Fragments
    // ...
    public void service() throws IOException {
        OutputStream output = new FileOutputStream("...");
        Runnable r = new Runnable() {
            public void run() {
                try { doService(); } catch (IOException e) {  }
            }
        };
        new ThreadWithOutputStream(r, output).start();
    }

    void doService() throws IOException {
        ThreadWithOutputStream.current().getOutput().write(0);
    }
}

class ServiceUsingThreadLocal {                   // Fragments
    static ThreadLocal output = new ThreadLocal();

    public void service() {
        try {
            final OutputStream s = new FileOutputStream("...");
            Runnable r = new Runnable() {
                public void run() {
                    output.set(s);
                    try { doService(); }
                    catch (IOException e) {  }

                    finally {
                        try { s.close(); }
                        catch (IOException ignore) {}
                    }
                }
            };
            new Thread(r).start();
        }
        catch (IOException e) {}
    }

    void doService() throws IOException {
        ((OutputStream)(output.get())).write(0);
        // ...
    }
}
class BarePoint {
    public double x;
    public double y;

}

class SynchedPoint {

    protected final BarePoint delegate = new BarePoint();

    public synchronized double getX() { return delegate.x;}
    public synchronized double getY() { return delegate.y; }
    public synchronized void setX(double v) { delegate.x = v; }
    public synchronized void setY(double v) { delegate.y = v; }
}

class Address {                          // Fragments
    protected String street;
    protected String city;

    public String getStreet() { return street; }
    public void setStreet(String s) { street = s; }
    // ...
    public void printLabel(OutputStream s) { }
}

class SynchronizedAddress extends Address {
    // ...
    public synchronized String getStreet() {
        return super.getStreet();
    }
    public synchronized void setStreet(String s) {
        super.setStreet(s);
    }

    public synchronized void printLabel(OutputStream s) {
        super.printLabel(s);
    }
}

class Printer {
    public void printDocument(byte[] doc) { /* ... */ }
    // ...
}

class PrintService {

    protected PrintService neighbor = null; // node to take from
    protected Printer printer = null;

    public synchronized void print(byte[] doc) {
        getPrinter().printDocument(doc);
    }

    protected Printer getPrinter() {    // PRE: synch lock held
        if (printer == null)  // need to take from neighbor
            printer = neighbor.takePrinter();
        return printer;
    }

    synchronized Printer takePrinter() { // called from others
        if (printer != null) {
            Printer p = printer; // implement take protocol
            printer = null;
            return p;
        }
        else
            return neighbor.takePrinter(); // propagate
    }

    // initialization methods called only during start-up

    synchronized void setNeighbor(PrintService n) {
        neighbor = n;
    }

    synchronized void givePrinter(Printer p) {
        printer = p;
    }

    // Sample code to initialize a ring of new services

    public static void startUpServices(int nServices, Printer p)
            throws IllegalArgumentException {

        if (nServices <= 0 || p == null)
            throw new IllegalArgumentException();

        PrintService first = new PrintService();
        PrintService pred = first;

        for (int i = 1; i < nServices; ++i) {
            PrintService s = new PrintService();
            s.setNeighbor(pred);
            pred = s;
        }

        first.setNeighbor(pred);
        first.givePrinter(p);
    }
}


class AnimationApplet extends Applet {            // Fragments
    // ...
    int framesPerSecond; // default zero is illegal value

    void animate() {

        try {
            if (framesPerSecond == 0) { // the unsynchronized check
                synchronized(this) {
                    if (framesPerSecond == 0) { // the double-check
                        String param = getParameter("fps");
                        framesPerSecond = Integer.parseInt(param);
                    }
                }
            }
        }
        catch (Exception e) {}

        // ... actions using framesPerSecond ...
    }
}



class ServerWithStateUpdate {
    private double state;
    private final Helper helper = new Helper();

    public synchronized void service() {
        state = 2.0f; // ...; // set to some new value
        helper.operation();
    }

    public synchronized double getState() { return state; }
}

class ServerWithOpenCall {
    private double state;
    private final Helper helper = new Helper();

    private synchronized void updateState() {
        state =  2.0f; //  ...; // set to some new value
    }

    public void service() {
        updateState();
        helper.operation();
    }

    public synchronized double getState() { return state; }
}

class ServerWithAssignableHelper {
    private double state;
    private Helper helper = new Helper();

    synchronized void setHelper(Helper h) { helper = h; }

    public void service() {
        Helper h;
        synchronized(this) {
            state = 2.0f; // ...
            h = helper;
        }
        h.operation();
    }

    public synchronized void synchedService() { // see below
        service();
    }
}

class LinkedCell {
    protected int value;
    protected final LinkedCell next;

    public LinkedCell(int v, LinkedCell t) {
        value = v;
        next = t;
    }

    public synchronized int value() { return value; }
    public synchronized void setValue(int v) { value = v; }

    public int sum() {               // add up all element values
        return (next == null) ? value() : value() + next.sum();
    }

    public boolean includes(int x) { // search for x
        return (value() == x) ? true:
                (next == null)? false : next.includes(x);
    }
}

class Shape {                                   // Incomplete
    protected double x = 0.0;
    protected double y = 0.0;
    protected double width = 0.0;
    protected double height = 0.0;

    public synchronized double x()      { return x;}
    public synchronized double y()      { return y; }
    public synchronized double width()  { return width;}
    public synchronized double height() { return height; }

    public synchronized void adjustLocation() {
        x = 1; // longCalculation1();
        y = 2; //longCalculation2();
    }

    public synchronized void adjustDimensions() {
        width = 3; // longCalculation3();
        height = 4; // longCalculation4();
    }

    // ...
}


class PassThroughShape {

    protected final AdjustableLoc loc = new AdjustableLoc(0, 0);
    protected final AdjustableDim dim = new AdjustableDim(0, 0);

    public double x()              { return loc.x(); }
    public double y()              { return loc.y(); }

    public double width()          { return dim.width(); }
    public double height()         { return dim.height(); }

    public void adjustLocation()   { loc.adjust(); }
    public void adjustDimensions() { dim.adjust(); }
}

class AdjustableLoc {
    protected double x;
    protected double y;

    public AdjustableLoc(double initX, double initY) {
        x = initX;
        y = initY;
    }

    public synchronized double x() { return x;}
    public synchronized double y() { return y; }

    public synchronized void adjust() {
        x = longCalculation1();
        y = longCalculation2();
    }

    protected double longCalculation1() { return 1; /* ... */ }
    protected double longCalculation2() { return 2; /* ... */ }

}


class AdjustableDim {
    protected double width;
    protected double height;

    public AdjustableDim(double initW, double initH) {
        width = initW;
        height = initH;
    }

    public synchronized double width() { return width;}
    public synchronized double height() { return height; }

    public synchronized void adjust() {
        width = longCalculation3();
        height = longCalculation4();
    }

    protected double longCalculation3() { return 3; /* ... */ }
    protected double longCalculation4() { return 4; /* ... */ }

}

class LockSplitShape {                     // Incomplete
    protected double x = 0.0;
    protected double y = 0.0;
    protected double width = 0.0;
    protected double height = 0.0;

    protected final Object locationLock = new Object();
    protected final Object dimensionLock = new Object();

    public double x() {
        synchronized(locationLock) {
            return x;
        }
    }

    public double y() {
        synchronized(locationLock) {
            return y;
        }
    }

    public void adjustLocation() {
        synchronized(locationLock) {
            x = 1; // longCalculation1();
            y = 2; // longCalculation2();
        }
    }

    // and so on

}


class SynchronizedInt {
    private int value;

    public SynchronizedInt(int v) { value = v; }

    public synchronized int get() { return value; }

    public synchronized int set(int v) { // returns previous value
        int oldValue = value;
        value = v;
        return oldValue;
    }

    public synchronized int increment() { return ++value; }

    // and so on

}

class Person {                             // Fragments
    // ...
    protected final SynchronizedInt age = new SynchronizedInt(0);

    protected final SynchronizedBoolean isMarried =
            new SynchronizedBoolean(false);

    protected final SynchronizedDouble income =
            new SynchronizedDouble(0.0);

    public int getAge() { return age.get(); }

    public void birthday() { age.increment(); }
    // ...
}

class LinkedQueue {
    protected Node head = new Node(null);
    protected Node last = head;

    protected final Object pollLock = new Object();
    protected final Object putLock = new Object();

    public void put(Object x) {
        Node node = new Node(x);
        synchronized (putLock) {     // insert at end of list
            synchronized (last) {
                last.next = node;        // extend list
                last = node;
            }
        }
    }

    public Object poll() {         // returns null if empty
        synchronized (pollLock) {
            synchronized (head) {
                Object x = null;
                Node first = head.next;  // get to first real node
                if (first != null) {
                    x = first.object;
                    first.object = null;   // forget old object
                    head = first;            // first becomes new head
                }
                return x;
            }
        }
    }

    static class Node {            // local node class for queue
        Object object;
        Node next = null;

        Node(Object x) { object = x; }
    }
}

class InsufficientFunds extends Exception {}

interface Account {
    long balance();
}

interface UpdatableAccount extends Account {
    void credit(long amount) throws InsufficientFunds;
    void debit(long amount) throws InsufficientFunds;
}

// Sample implementation of updatable version
class UpdatableAccountImpl implements UpdatableAccount {
    private long currentBalance;

    public UpdatableAccountImpl(long initialBalance) {
        currentBalance = initialBalance;
    }

    public synchronized long balance() {
        return currentBalance;
    }

    public synchronized void credit(long amount)
            throws InsufficientFunds {
        if (amount >= 0 || currentBalance >= -amount)
            currentBalance += amount;
        else
            throw new InsufficientFunds();
    }

    public synchronized void debit(long amount)
            throws InsufficientFunds {
        credit(-amount);
    }
}

final class ImmutableAccount implements Account {
    private Account delegate;

    public ImmutableAccount(long initialBalance) {
        delegate = new UpdatableAccountImpl(initialBalance);
    }

    ImmutableAccount(Account acct) { delegate = acct; }

    public long balance() { // forward the immutable method
        return delegate.balance();
    }
}
class AccountRecorder { // A logging facility
    public void recordBalance(Account a) {
        System.out.println(a.balance()); // or record in file
    }
}

class AccountHolder {
    private UpdatableAccount acct = new UpdatableAccountImpl(0);
    private AccountRecorder recorder;

    public AccountHolder(AccountRecorder r) {
        recorder = r;
    }

    public synchronized void acceptMoney(long amount) {
        try {
            acct.credit(amount);
            recorder.recordBalance(new ImmutableAccount(acct));//(*)
        }
        catch (InsufficientFunds ex) {
            System.out.println("Cannot accept negative amount.");
        }
    }
}

class EvilAccountRecorder extends AccountRecorder {
    private long embezzlement;
    // ...
    public void recordBalance(Account a) {
        super.recordBalance(a);

        if (a instanceof UpdatableAccount) {
            UpdatableAccount u = (UpdatableAccount)a;
            try {
                u.debit(10);
                embezzlement += 10;
            }
            catch (InsufficientFunds quietlyignore) {}
        }
    }
}
class ImmutablePoint {
    private final int x;
    private final int y;

    public ImmutablePoint(int initX, int initY) {
        x = initX;
        y = initY;
    }

    public int x() { return x; }
    public int y() { return y; }
}

class Dot {
    protected ImmutablePoint loc;

    public Dot(int x, int y) {
        loc = new ImmutablePoint(x, y);
    }

    public synchronized ImmutablePoint location() { return loc; }

    protected synchronized void updateLoc(ImmutablePoint newLoc) {
        loc = newLoc;
    }

    public void moveTo(int x, int y) {
        updateLoc(new ImmutablePoint(x, y));
    }

    public synchronized void shiftX(int delta) {
        updateLoc(new ImmutablePoint(loc.x() + delta,
                loc.y()));
    }
}
class CopyOnWriteArrayList {         // Incomplete
    protected Object[] array = new Object[0];

    protected synchronized Object[] getArray() { return array; }

    public synchronized void add(Object element) {
        int len = array.length;
        Object[] newArray = new Object[len+1];
        System.arraycopy(array, 0, newArray, 0, len);
        newArray[len] = element;
        array = newArray;
    }

    public Iterator iterator() {
        return new Iterator() {
            protected final Object[] snapshot = getArray();
            protected int cursor = 0;

            public boolean hasNext() {
                return cursor < snapshot.length;
            }

            public Object next() {
                try {
                    return snapshot[cursor++];
                }
                catch (IndexOutOfBoundsException ex) {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {}

        };
    }
}

class State {}

class Optimistic {                       // Generic code sketch

    private State state; // reference to representation object

    private synchronized State getState() { return state; }

    private synchronized boolean commit(State assumed,
                                        State next) {
        if (state == assumed) {
            state = next;
            return true;
        }
        else
            return false;
    }
}

class OptimisticDot {
    protected ImmutablePoint loc;

    public OptimisticDot(int x, int y) {
        loc = new ImmutablePoint(x, y);
    }

    public synchronized ImmutablePoint location() { return loc; }

    protected synchronized boolean commit(ImmutablePoint assumed,
                                          ImmutablePoint next) {
        if (loc == assumed) {
            loc = next;
            return true;
        }
        else
            return false;
    }

    public synchronized void moveTo(int x, int y) {
        // bypass commit since unconditional
        loc = new ImmutablePoint(x, y);
    }

    public void shiftX(int delta) {
        boolean success = false;
        do {
            ImmutablePoint old = location();
            ImmutablePoint next = new ImmutablePoint(old.x() + delta,
                    old.y());
            success = commit(old, next);
        } while (!success);
    }

}


class ParticleUsingMutex {
    protected int x;
    protected int y;
    protected final Random rng = new Random();
    protected final Mutex mutex = new Mutex();

    public ParticleUsingMutex(int initialX, int initialY) {
        x = initialX;
        y = initialY;
    }

    public void move() {
        try {
            mutex.acquire();
            try {
                x += rng.nextInt(10) - 5;
                y += rng.nextInt(20) - 10;
            }
            finally { mutex.release(); }
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void draw(Graphics g) {
        int lx, ly;

        try {
            mutex.acquire();
            try {
                lx = x; ly = y;
            }
            finally { mutex.release(); }
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return;
        }

        g.drawRect(lx, ly, 10, 10);
    }
}


class WithMutex {
    private final Mutex mutex;
    public WithMutex(Mutex m) { mutex = m; }

    public void perform(Runnable r) throws InterruptedException {
        mutex.acquire();
        try     { r.run(); }
        finally { mutex.release(); }
    }
}
class ParticleUsingWrapper {                     // Incomplete

    protected int x;
    protected int y;
    protected final Random rng = new Random();

    protected final WithMutex withMutex =
            new WithMutex(new Mutex());

    protected void doMove() {
        x += rng.nextInt(10) - 5;
        y += rng.nextInt(20) - 10;
    }

    public void move() {
        try {
            withMutex.perform(new Runnable() {
                public void run() { doMove(); }
            });
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    // ...
}


class CellUsingBackoff {
    private long value;
    private final Mutex mutex = new Mutex();

    void swapValue(CellUsingBackoff other) {
        if (this == other) return; // alias check required
        for (;;) {
            try {
                mutex.acquire();

                try {
                    if (other.mutex.attempt(0)) {
                        try {
                            long t = value;
                            value = other.value;
                            other.value = t;
                            return;
                        }
                        finally {
                            other.mutex.release();
                        }
                    }
                }
                finally {
                    mutex.release();
                };

                Thread.sleep(100);
            }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}


class CellUsingReorderedBackoff {
    private long value;
    private final Mutex mutex = new Mutex();

    private static boolean trySwap(CellUsingReorderedBackoff a,
                                   CellUsingReorderedBackoff b)
            throws InterruptedException {
        boolean success = false;

        if (a.mutex.attempt(0)) {
            try {
                if (b.mutex.attempt(0)) {
                    try {
                        long t = a.value;
                        a.value = b.value;
                        b.value = t;
                        success = true;
                    }
                    finally {
                        b.mutex.release();
                    }
                }
            }
            finally {
                a.mutex.release();
            }
        }

        return success;

    }

    void swapValue(CellUsingReorderedBackoff other) {
        if (this == other) return; // alias check required
        try {
            while (!trySwap(this, other) &&
                    !trySwap(other, this))
                Thread.sleep(100);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

class ListUsingMutex {

    static class Node {
        Object item;
        Node next;
        Mutex lock = new Mutex(); // each node keeps its own lock
        Node(Object x, Node n) { item = x; next = n; }
    }

    protected Node head; // pointer to first node of list

    // Use plain synchronization to protect head field.
    //  (We could instead use a Mutex here too but there is no
    //  reason to do so.)

    protected synchronized Node getHead() { return head; }

    public synchronized void add(Object x) { // simple prepend

        // for simplicity here, do not allow null elements
        if (x == null) throw new IllegalArgumentException();

        // The use of synchronized here protects only head field.
        // The method does not need to wait out other traversers
        // who have already made it past head node.

        head = new Node(x, head);
    }
    boolean search(Object x) throws InterruptedException {
        Node p = getHead();

        if (p == null || x == null) return false;

        p.lock.acquire();  // Prime loop by acquiring first lock.

        // If above acquire fails due to interrupt, the method will
        //   throw InterruptedException now, so there is no need for
        //   further cleanup.

        for (;;) {
            Node nextp = null;
            boolean found;

            try {
                found = x.equals(p.item);
                if (!found) {
                    nextp = p.next;
                    if (nextp != null) {
                        try {           // Acquire next lock
                            //   while still holding current
                            nextp.lock.acquire();
                        }
                        catch (InterruptedException ie) {
                            throw ie;     // Note that finally clause will
                            //   execute before the throw
                        }
                    }
                }
            }
            finally {
                p.lock.release();
            }

            if (found)
                return true;
            else if (nextp == null)
                return false;
            else
                p = nextp;
        }
    }

    // ...  other similar traversal and update methods ...
}

class DataRepository {                           // code sketch

    protected final ReadWriteLock rw = new WriterPreferenceReadWriteLock();

    public void access() throws InterruptedException {
        rw.readLock().acquire();
        try {
      /* read data */
        }
        finally {
            rw.readLock().release();
        }
    }

    public void modify() throws InterruptedException {
        rw.writeLock().acquire();
        try {
      /* write data */
        }
        finally {
            rw.writeLock().release();
        }
    }

}

class ClientUsingSocket {                       // Code sketch
    int portnumber = 1234;
    String server = "gee";
    // ...
    Socket retryUntilConnected() throws InterruptedException {
        // first delay is randomly chosen between 5 and 10secs
        long delayTime = 5000 + (long)(Math.random() * 5000);
        for (;;) {
            try {
                return new Socket(server, portnumber);
            }
            catch (IOException ex) {
                Thread.sleep(delayTime);
                delayTime = delayTime * 3 / 2 + 1; // increase 50%
            }
        }
    }
}

class ServiceException extends Exception {}

interface ServerWithException {
    void service() throws ServiceException;
}

interface ServiceExceptionHandler {
    void handle(ServiceException e);
}

class ServerImpl implements ServerWithException {
    public void service() throws ServiceException {}
}

class HandlerImpl implements ServiceExceptionHandler {
    public void handle(ServiceException e) {}
}



class HandledService implements ServerWithException {
    final ServerWithException server = new ServerImpl();
    final ServiceExceptionHandler handler = new HandlerImpl();

    public void service() { // no throw clause
        try {
            server.service();
        }
        catch (ServiceException e) {
            handler.handle(e);
        }
    }
}

class ExceptionEvent extends java.util.EventObject {
    public final Throwable theException;

    public ExceptionEvent(Object src, Throwable ex) {
        super(src);
        theException = ex;
    }
}


class ExceptionEventListener {                // Incomplete
    public void exceptionOccured(ExceptionEvent ee) {
        // ... respond to exception...
    }
}

class ServiceIssuingExceptionEvent {         // Incomplete
    // ...
    private final CopyOnWriteArrayList handlers =
            new CopyOnWriteArrayList();

    public void addHandler(ExceptionEventListener h) {
        handlers.add(h);
    }

    public void service() {
        // ...
        boolean failed = true;
        if (failed) {
            Throwable ex = new ServiceException();
            ExceptionEvent ee = new ExceptionEvent(this, ex);

            for (Iterator it = handlers.iterator(); it.hasNext();) {
                ExceptionEventListener l =
                        (ExceptionEventListener)(it.next());
                l.exceptionOccured(ee);
            }
        }
    }

}

class CancellableReader {                        // Incomplete
    private Thread readerThread; // only one at a time supported
    private FileInputStream dataFile;

    public synchronized void startReaderThread()
            throws IllegalStateException, FileNotFoundException {
        if (readerThread != null) throw new IllegalStateException();
        dataFile = new FileInputStream("data");
        readerThread = new Thread(new Runnable() {
            public void run() { doRead(); }
        });
        readerThread.start();
    }

    protected synchronized void closeFile() { // utility method
        if (dataFile != null) {
            try { dataFile.close(); }
            catch (IOException ignore) {}
            dataFile = null;
        }
    }

    void process(int b) {}

    private void doRead() {
        try {
            while (!Thread.interrupted()) {
                try {
                    int c = dataFile.read();
                    if (c == -1) break;
                    else process(c);
                }
                catch (IOException ex) {
                    break; // perhaps first do other cleanup
                }
            }
        }
        finally {
            closeFile();
            synchronized(this) { readerThread = null; }
        }
    }

    public synchronized void cancelReaderThread() {
        if (readerThread != null) readerThread.interrupt();
        closeFile();
    }
}
class ReaderWithTimeout {               // Generic code sketch
    // ...
    void process(int b) {}

    void attemptRead(InputStream stream, long timeout) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            for (;;) {
                if (stream.available() > 0) {
                    int c = stream.read();
                    if (c != -1) process(c);
                    else  break; // eof
                }
                else {
                    try {
                        Thread.sleep(100); // arbitrary back-off time
                    }
                    catch (InterruptedException ie) {
            /* ... quietly wrap up and return ... */
                    }
                    long now = System.currentTimeMillis();
                    if (now - startTime >= timeout) {
            /* ... fail ...*/
                    }
                }
            }
        }
        catch (IOException ex) { /* ... fail ... */ }
    }
}

class C {                                         // Fragments
    private int v;  // invariant: v >= 0

    synchronized void f() {
        v = -1  ;   // temporarily set to illegal value as flag
        compute();  // possible stop point (*)
        v = 1;      // set to legal value
    }

    synchronized void g() {
        while (v != 0) {
            --v;
            something();
        }
    }

    void compute() {}
    void something() {}
}

class Terminator {

    // Try to kill; return true if known to be dead

    static boolean terminate(Thread t, long maxWaitToDie) {

        if (!t.isAlive()) return true;  // already dead

        // phase 1 -- graceful cancellation
        t.interrupt();
        try { t.join(maxWaitToDie); }
        catch(InterruptedException e){} //  ignore

        if (!t.isAlive()) return true;  // success

        // phase 2 -- trap all security checks
        // theSecurityMgr.denyAllChecksFor(t); // a made-up method
        try { t.join(maxWaitToDie); }
        catch(InterruptedException ex) {}

        if (!t.isAlive()) return true;

        // phase 3 -- minimize damage
        t.setPriority(Thread.MIN_PRIORITY);
        return false;
    }

}

interface BoundedCounter {

    static final long MIN = 0;  // minimum allowed value

    static final long MAX = 10; // maximum allowed value


    long count();         // INV:  MIN <= count() <= MAX
    // INIT: count() == MIN

    void inc();           // only allowed when count() < MAX

    void dec();           // only allowed when count() > MIN

}

class X {
    synchronized void w() throws InterruptedException {
        before(); wait(); after();
    }
    synchronized void n() { notifyAll(); }
    void before() {}
    void after() {}
}


class GuardedClass {                     // Generic code sketch
    protected boolean cond = false;

    // PRE: lock held
    protected void awaitCond() throws InterruptedException{
        while (!cond) wait();
    }

    public synchronized void guardedAction() {
        try {
            awaitCond();
        }
        catch (InterruptedException ie) {
            // fail
        }

        // actions
    }
}


class SimpleBoundedCounter {

    static final long MIN = 0;  // minimum allowed value

    static final long MAX = 10; // maximum allowed value

    protected long count = MIN;

    public synchronized long count() { return count; }

    public synchronized void inc() throws InterruptedException {
        awaitUnderMax();
        setCount(count + 1);
    }

    public synchronized void dec() throws InterruptedException {
        awaitOverMin();
        setCount(count - 1);
    }

    protected void setCount(long newValue) { // PRE: lock held
        count = newValue;
        notifyAll(); // wake up any thread depending on new value
    }

    protected void awaitUnderMax() throws InterruptedException {
        while (count == MAX) wait();
    }

    protected void awaitOverMin() throws InterruptedException {
        while (count == MIN) wait();
    }
}


class GuardedClassUsingNotify {
    protected boolean cond = false;
    protected int nWaiting = 0; // count waiting threads

    protected synchronized void awaitCond()
            throws InterruptedException {
        while (!cond) {
            ++nWaiting; // record fact that a thread is waiting
            try {
                wait();
            }
            catch (InterruptedException ie) {
                notify();
                throw ie;
            }
            finally {
                --nWaiting; // no longer waiting
            }
        }
    }

    protected synchronized void signalCond() {
        if (cond) {                 // simulate notifyAll
            for (int i = nWaiting; i > 0; --i) {
                notify();
            }
        }
    }
}


class GamePlayer implements Runnable {          // Incomplete
    protected GamePlayer other;
    protected boolean myturn = false;

    protected synchronized void setOther(GamePlayer p) {
        other = p;
    }

    synchronized void giveTurn() { // called by other player
        myturn = true;
        notify();                    // unblock thread
    }

    void releaseTurn() {
        GamePlayer p;
        synchronized(this) {
            myturn = false;
            p = other;
        }
        p.giveTurn(); // open call
    }

    synchronized void awaitTurn() throws InterruptedException {
        while (!myturn) wait();
    }

    void move() { /*... perform one move ... */ }

    public void run() {
        try {
            for (;;) {
                awaitTurn();
                move();
                releaseTurn();
            }
        }
        catch (InterruptedException ie) {} // die
    }

    public static void main(String[] args) {
        GamePlayer one = new GamePlayer();
        GamePlayer two = new GamePlayer();
        one.setOther(two);
        two.setOther(one);
        one.giveTurn();
        new Thread(one).start();
        new Thread(two).start();
    }
}
//class TimeoutException extends InterruptedException { ... }

class TimeOutBoundedCounter {

    static final long MIN = 0;  // minimum allowed value

    static final long MAX = 10; // maximum allowed value

    protected long count = 0;

    protected long TIMEOUT = 5000; // for illustration

    // ...
    synchronized void inc() throws InterruptedException {

        if (count >= MAX) {
            long start = System.currentTimeMillis();
            long waitTime = TIMEOUT;

            for (;;) {
                if (waitTime <= 0)
                    throw new TimeoutException(TIMEOUT);
                else {
                    try {
                        wait(waitTime);
                    }
                    catch (InterruptedException ie) {
                        throw ie;  // coded this way just for emphasis
                    }
                    if (count < MAX)
                        break;
                    else {
                        long now = System.currentTimeMillis();
                        waitTime = TIMEOUT - (now - start);
                    }
                }
            }
        }

        ++count;
        notifyAll();
    }

    synchronized void dec() throws InterruptedException {
        // ... similar ...
    }

}
class SpinLock {                   // Avoid needing to use this

    private volatile boolean busy = false;

    synchronized void release() { busy = false; }

    void acquire() throws InterruptedException {
        int itersBeforeYield = 100;    // 100 is arbitrary
        int itersBeforeSleep = 200;    // 200 is arbitrary
        long sleepTime = 1;            // 1msec is arbitrary
        int iters = 0;
        for (;;) {
            if (!busy) {                 // test-and-test-and-set
                synchronized(this) {
                    if (!busy) {
                        busy = true;
                        return;
                    }
                }
            }

            if (iters < itersBeforeYield) {       // spin phase
                ++iters;
            }
            else if (iters < itersBeforeSleep) {  // yield phase
                ++iters;
                Thread.yield();
            }
            else {                                // back-off phase
                Thread.sleep(sleepTime);
                sleepTime =  3 * sleepTime / 2 + 1; // 50% is arbitrary
            }
        }
    }
}



class BoundedBufferWithStateTracking {
    protected final Object[]  array;    // the elements
    protected int putPtr = 0;           // circular indices
    protected int takePtr = 0;
    protected int usedSlots = 0;        // the count

    public BoundedBufferWithStateTracking(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0) throw new IllegalArgumentException();
        array = new Object[capacity];
    }

    public synchronized int size() { return usedSlots; }

    public int capacity() { return array.length; }

    public synchronized void put(Object x)
            throws InterruptedException {

        while (usedSlots == array.length) // wait until not full
            wait();

        array[putPtr] = x;
        putPtr = (putPtr + 1) % array.length; // cyclically inc

        if (usedSlots++ == 0)              // signal if was empty
            notifyAll();
    }

    public synchronized Object take()
            throws InterruptedException{

        while (usedSlots == 0)           // wait until not empty
            wait();

        Object x = array[takePtr];
        array[takePtr] = null;
        takePtr = (takePtr + 1) % array.length;

        if (usedSlots-- == array.length) // signal if was full
            notifyAll();
        return x;
    }

}

class BoundedCounterWithStateVariable  {

    static final long MIN = 0;  // minimum allowed value

    static final long MAX = 10; // maximum allowed value


    static final int BOTTOM = 0, MIDDLE = 1, TOP = 2;
    protected int state = BOTTOM;  // the state variable
    protected long count = MIN;

    protected void updateState() { // PRE: synch lock held
        int oldState = state;
        if      (count == MIN) state = BOTTOM;
        else if (count == MAX) state = TOP;
        else                   state = MIDDLE;
        if (state != oldState && oldState != MIDDLE)
            notifyAll();              // notify on transition
    }

    public synchronized long count() { return count; }

    public synchronized void inc() throws InterruptedException {
        while (state == TOP) wait();
        ++count;
        updateState();
    }

    public synchronized void dec() throws InterruptedException {
        while (state == BOTTOM) wait();
        --count;
        updateState();
    }
}

class Inventory {

    protected final Hashtable items = new Hashtable();
    protected final Hashtable suppliers = new Hashtable();

    // execution state tracking variables:

    protected int storing = 0;    // number of in-progress stores
    protected int retrieving = 0; // number of retrieves

    // ground actions:

    protected void doStore(String description, Object item,
                           String supplier) {
        items.put(description, item);
        suppliers.put(supplier, description);
    }

    protected Object doRetrieve(String description) {
        Object x = items.get(description);
        if (x != null)
            items.remove(description);
        return x;
    }
    public void store(String description,
                      Object item,
                      String supplier)
            throws InterruptedException {

        synchronized(this) {                    // Before-action
            while (retrieving != 0) // don't overlap with retrieves
                wait();
            ++storing;                           // record exec state
        }

        try {
            doStore(description, item, supplier); // Ground action
        }

        finally {                               // After-action
            synchronized(this) {                  // signal retrieves
                if (--storing == 0) // only necessary when hit zero
                    notifyAll();
            }
        }
    }

    public Object retrieve(String description)
            throws InterruptedException {

        synchronized(this) {                    // Before-action
            // wait until no stores or retrieves
            while (storing != 0 || retrieving != 0)
                wait();
            ++retrieving;
        }

        try {
            return doRetrieve(description);       // ground action
        }

        finally {
            synchronized(this) {                  // After-action
                if (--retrieving == 0)
                    notifyAll();
            }
        }
    }
}

abstract class ReadWrite {
    protected int activeReaders = 0;  // threads executing read
    protected int activeWriters = 0;  // always zero or one

    protected int waitingReaders = 0; // threads not yet in read
    protected int waitingWriters = 0; // same for write

    protected abstract void doRead(); // implement in subclasses
    protected abstract void doWrite();

    public void read() throws InterruptedException {
        beforeRead();
        try     { doRead(); }
        finally { afterRead(); }
    }

    public void write() throws InterruptedException {
        beforeWrite();
        try     { doWrite(); }
        finally { afterWrite(); }
    }
    protected boolean allowReader() {
        return waitingWriters == 0 && activeWriters == 0;
    }

    protected boolean allowWriter() {
        return activeReaders == 0 && activeWriters == 0;
    }

    protected synchronized void beforeRead()
            throws InterruptedException {
        ++waitingReaders;
        while (!allowReader()) {
            try { wait(); }
            catch (InterruptedException ie) {
                --waitingReaders; // roll back state
                throw ie;
            }
        }
        --waitingReaders;
        ++activeReaders;
    }

    protected synchronized void afterRead()  {
        --activeReaders;
        notifyAll();
    }

    protected synchronized void beforeWrite()
            throws InterruptedException {
        ++waitingWriters;
        while (!allowWriter()) {
            try { wait(); }
            catch (InterruptedException ie) {
                --waitingWriters;
                throw ie;
            }
        }
        --waitingWriters;
        ++activeWriters;
    }

    protected synchronized void afterWrite() {
        --activeWriters;
        notifyAll();
    }
}
class RWLock extends ReadWrite implements ReadWriteLock { // Incomplete
    class RLock implements Sync {
        public void acquire() throws InterruptedException {
            beforeRead();
        }

        public void release() {
            afterRead();
        }

        public boolean attempt(long msecs)
                throws InterruptedException{
            return beforeRead(msecs);
        }
    }

    class WLock implements Sync {
        public void acquire() throws InterruptedException {
            beforeWrite();
        }

        public void release() {
            afterWrite();
        }

        public boolean attempt(long msecs)
                throws InterruptedException{
            return beforeWrite(msecs);
        }
    }

    protected final RLock rlock = new RLock();
    protected final WLock wlock = new WLock();

    public Sync readLock()  { return rlock; }
    public Sync writeLock() { return wlock; }

    public boolean beforeRead(long msecs)
            throws InterruptedException {
        return true;
        // ... time-out version of beforeRead ...
    }

    public boolean beforeWrite(long msecs)
            throws InterruptedException {
        return true;
        // ... time-out version of beforeWrite ...
    }

    protected void doRead() {}
    protected void doWrite() {}

}

class StackEmptyException extends Exception { }

class Stack {                                    // Fragments

    public synchronized boolean isEmpty() { return false; /* ... */ }

    public synchronized void push(Object x) { /* ... */ }

    public synchronized Object pop() throws StackEmptyException {
        if (isEmpty())
            throw new StackEmptyException();
        else return null;
    }
}

class WaitingStack extends Stack {

    public synchronized void push(Object x) {
        super.push(x);
        notifyAll();
    }

    public synchronized Object waitingPop()
            throws InterruptedException {

        while (isEmpty()) {
            wait();
        }

        try {
            return super.pop();
        }
        catch (StackEmptyException cannothappen) {
            // only possible if pop contains a programming error
            throw new Error("Internal implementation error");
        }
    }

}

class PartWithGuard {
    protected boolean cond = false;

    synchronized void await() throws InterruptedException {
        while (!cond)
            wait();
        // any other code
    }

    synchronized void signal(boolean c) {
        cond = c;
        notifyAll();
    }
}

class Host {
    protected final PartWithGuard part = new PartWithGuard();

    synchronized void rely() throws InterruptedException {
        part.await();
    }

    synchronized void set(boolean c) {
        part.signal(c);
    }
}

class OwnedPartWithGuard {                      // Code sketch
    protected boolean cond = false;
    final Object lock;
    OwnedPartWithGuard(Object owner) { lock = owner; }

    void await() throws InterruptedException {
        synchronized(lock) {
            while (!cond)
                lock.wait();
            // ...
        }
    }

    void signal(boolean c) {
        synchronized(lock) {
            cond = c;
            lock.notifyAll();
        }
    }
}


class Pool {                                     // Incomplete

    protected java.util.ArrayList items = new ArrayList();
    protected java.util.HashSet busy = new HashSet();

    protected final Semaphore available;

    public Pool(int n) {
        available = new Semaphore(n);
        initializeItems(n);
    }

    public Object getItem() throws InterruptedException {
        available.acquire();
        return doGet();
    }

    public void returnItem(Object x) {
        if (doReturn(x))
            available.release();
    }

    protected synchronized Object doGet() {
        Object x = items.remove(items.size()-1);
        busy.add(x); // put in set to check returns
        return x;
    }

    protected synchronized boolean doReturn(Object x) {
        if (busy.remove(x)) {
            items.add(x); // put back into available item list
            return true;
        }
        else return false;
    }

    protected void initializeItems(int n) {
        // Somehow create the resource objects
        //   and place them in items list.
    }
}

class BufferArray {
    protected final Object[]  array;      // the elements
    protected int putPtr = 0;             // circular indices
    protected int takePtr = 0;
    BufferArray(int n) { array = new Object[n]; }

    synchronized void insert(Object x) {  // put mechanics
        array[putPtr] = x;
        putPtr = (putPtr + 1) % array.length;
    }

    synchronized Object extract() {       // take mechanics
        Object x = array[takePtr];
        array[takePtr] = null;
        takePtr = (takePtr + 1) % array.length;
        return x;
    }
}

class BoundedBufferWithSemaphores {
    protected final BufferArray buff;
    protected final Semaphore putPermits;
    protected final Semaphore takePermits;

    public BoundedBufferWithSemaphores(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0) throw new IllegalArgumentException();
        buff = new BufferArray(capacity);
        putPermits = new Semaphore(capacity);
        takePermits = new Semaphore(0);
    }

    public void put(Object x) throws InterruptedException {
        putPermits.acquire();
        buff.insert(x);
        takePermits.release();
    }

    public Object take() throws InterruptedException {
        takePermits.acquire();
        Object x = buff.extract();
        putPermits.release();
        return x;
    }

    public Object poll(long msecs) throws InterruptedException {
        if (!takePermits.attempt(msecs)) return null;
        Object x = buff.extract();
        putPermits.release();
        return x;
    }

    public boolean offer(Object x, long msecs)
            throws InterruptedException {
        if (!putPermits.attempt(msecs)) return false;
        buff.insert(x);
        takePermits.release();
        return true;
    }
}


class SynchronousChannel /* implements Channel */ {

    protected Object item = null; // to hold while in transit

    protected final Semaphore putPermit;
    protected final Semaphore takePermit;
    protected final Semaphore taken;

    public SynchronousChannel() {
        putPermit = new Semaphore(1);
        takePermit = new Semaphore(0);
        taken = new Semaphore(0);
    }

    public void put(Object x) throws InterruptedException {
        putPermit.acquire();
        item = x;
        takePermit.release();

        // Must wait until signalled by taker
        InterruptedException caught = null;
        for (;;) {
            try {
                taken.acquire();
                break;
            }
            catch(InterruptedException ie) { caught = ie; }
        }

        if (caught != null) throw caught; // can now rethrow
    }

    public Object take() throws InterruptedException {
        takePermit.acquire();
        Object x = item;
        item = null;
        putPermit.release();
        taken.release();
        return x;
    }
}

class Player implements Runnable {              // Code sketch
    // ...
    protected final Latch startSignal;

    Player(Latch l) { startSignal = l; }

    public void run() {
        try {
            startSignal.acquire();
            play();
        }
        catch(InterruptedException ie) { return; }
    }

    void play() {}
    // ...
}

class Game {
    // ...
    void begin(int nplayers) {
        Latch startSignal = new Latch();

        for (int i = 0; i < nplayers; ++i)
            new Thread(new Player(startSignal)).start();

        startSignal.release();
    }
}

class LatchingThermometer {                      // Seldom useful
    private volatile boolean ready; // latching
    private volatile float temperature;

    public double getReading() {
        while (!ready)
            Thread.yield();
        return temperature;
    }

    void sense(float t) { // called from sensor
        temperature = t;
        ready = true;
    }
}
class FillAndEmpty {                              // Incomplete
    static final int SIZE = 1024; // buffer size, for demo
    protected Rendezvous exchanger = new Rendezvous(2);

    protected byte readByte() { return 1; /* ... */ }
    protected void useByte(byte b) { /* ... */ }

    public void start() {
        new Thread(new FillingLoop()).start();
        new Thread(new EmptyingLoop()).start();
    }
    class FillingLoop implements Runnable { // inner class
        public void run() {
            byte[] buffer = new byte[SIZE];
            int position = 0;

            try {
                for (;;) {

                    if (position == SIZE) {
                        buffer = (byte[])(exchanger.rendezvous(buffer));
                        position = 0;
                    }

                    buffer[position++] = readByte();
                }
            }
            catch (BrokenBarrierException ex) {} // die
            catch (InterruptedException ie) {} // die
        }
    }

    class EmptyingLoop implements Runnable { // inner class
        public void run() {
            byte[] buffer = new byte[SIZE];
            int position = SIZE;  // force exchange first time through

            try {
                for (;;) {

                    if (position == SIZE) {
                        buffer = (byte[])(exchanger.rendezvous(buffer));
                        position = 0;
                    }

                    useByte(buffer[position++]);
                }
            }
            catch (BrokenBarrierException ex) {} // die
            catch (InterruptedException ex) {} // die
        }
    }

}

class PThreadsStyleBuffer {
    private final Mutex mutex = new Mutex();
    private final CondVar notFull = new CondVar(mutex);
    private final CondVar notEmpty = new CondVar(mutex);
    private int count = 0;
    private int takePtr = 0;
    private int putPtr = 0;
    private final Object[] array;

    public PThreadsStyleBuffer(int capacity) {
        array = new Object[capacity];
    }

    public void put(Object x) throws InterruptedException {
        mutex.acquire();
        try {
            while (count == array.length)
                notFull.await();

            array[putPtr] = x;
            putPtr = (putPtr + 1) % array.length;
            ++count;
            notEmpty.signal();
        }
        finally {
            mutex.release();
        }
    }

    public Object take() throws InterruptedException {
        Object x = null;
        mutex.acquire();
        try {
            while (count == 0)
                notEmpty.await();

            x = array[takePtr];
            array[takePtr] = null;
            takePtr = (takePtr + 1) % array.length;
            --count;
            notFull.signal();
        }
        finally {
            mutex.release();
        }
        return x;
    }
}


class BankAccount {
    protected long balance = 0;

    public synchronized long balance() {
        return balance;
    }

    public synchronized void deposit(long amount)
            throws InsufficientFunds {
        if (balance + amount < 0)
            throw new InsufficientFunds();
        else
            balance += amount;
    }

    public void withdraw(long amount) throws InsufficientFunds {
        deposit(-amount);
    }
}

class TSBoolean {
    private boolean value = false;

    // set to true; return old value
    public synchronized boolean testAndSet() {
        boolean oldValue = value;
        value = true;
        return oldValue;
    }

    public synchronized void clear() {
        value = false;
    }

}
class ATCheckingAccount extends BankAccount {
    protected ATSavingsAccount savings;
    protected long threshold;
    protected TSBoolean transferInProgress = new TSBoolean();

    public ATCheckingAccount(long t) { threshold = t; }

    // called only upon initialization
    synchronized void initSavings(ATSavingsAccount s) {
        savings = s;
    }

    protected boolean shouldTry() { return balance < threshold; }

    void tryTransfer() { // called internally or from savings
        if (!transferInProgress.testAndSet()) { // if not busy ...
            try {
                synchronized(this) {
                    if (shouldTry()) balance += savings.transferOut();
                }
            }
            finally { transferInProgress.clear(); }
        }
    }

    public synchronized void deposit(long amount)
            throws InsufficientFunds {
        if (balance + amount < 0)
            throw new InsufficientFunds();
        else {
            balance += amount;
            tryTransfer();
        }
    }
}

class ATSavingsAccount extends BankAccount {

    protected ATCheckingAccount checking;
    protected long maxTransfer;

    public ATSavingsAccount(long max) {
        maxTransfer = max;
    }

    // called only upon initialization
    synchronized void initChecking(ATCheckingAccount c) {
        checking = c;
    }

    synchronized long transferOut() { // called only from checking
        long amount = balance;
        if (amount > maxTransfer)
            amount = maxTransfer;
        if (amount >= 0)
            balance -= amount;
        return amount;
    }

    public synchronized void deposit(long amount)
            throws InsufficientFunds {
        if (balance + amount < 0)
            throw new InsufficientFunds();
        else {
            balance += amount;
            checking.tryTransfer();
        }
    }

}


class Subject {

    protected double val = 0.0;       // modeled state
    protected final EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList observers =
            new EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList();

    public synchronized double getValue() { return val; }
    protected synchronized void setValue(double d) { val = d; }

    public void attach(Observer o) { observers.add(o); }
    public void detach(Observer o) { observers.remove(o); }

    public void changeValue(double newstate) {
        setValue(newstate);
        for (Iterator it = observers.iterator(); it.hasNext();)
            ((Observer)(it.next())).changed(this);
    }

}

class Observer {

    protected double cachedState;      // last known state
    protected final Subject subj;      // only one allowed here

    Observer(Subject s) {
        subj = s;
        cachedState = s.getValue();
        display();
    }

    synchronized void changed(Subject s){
        if (s != subj) return;     // only one subject

        double oldState = cachedState;
        cachedState = subj.getValue(); // probe
        if (oldState != cachedState)
            display();
    }

    protected void display() {
        // somehow display subject state; for example just:
        System.out.println(cachedState);
    }

}

class Failure extends Exception {}

interface Transactor {

    // Enter a new transaction and return true, if can do so
    public boolean join(Transaction t);

    // Return true if this transaction can be committed
    public boolean canCommit(Transaction t);

    // Update state to reflect current transaction
    public void commit(Transaction t) throws Failure;

    // Roll back state (No exception; ignore if inapplicable)
    public void abort(Transaction t);

}

class Transaction {
    // add anything you want here
}

interface TransBankAccount extends Transactor {

    public long balance(Transaction t) throws Failure;

    public void deposit(Transaction t, long amount)
            throws InsufficientFunds, Failure;

    public void withdraw(Transaction t, long amount)
            throws InsufficientFunds, Failure;

}
class SimpleTransBankAccount implements TransBankAccount {

    protected long balance = 0;
    protected long workingBalance = 0; // single shadow copy
    protected Transaction currentTx = null; // single transaction

    public synchronized long balance(Transaction t) throws Failure {
        if (t != currentTx) throw new Failure();
        return workingBalance;
    }

    public synchronized void deposit(Transaction t, long amount)
            throws InsufficientFunds, Failure {
        if (t != currentTx) throw new Failure();
        if (workingBalance < -amount)
            throw new InsufficientFunds();
        workingBalance += amount;
    }

    public synchronized void withdraw(Transaction t, long amount)
            throws InsufficientFunds, Failure {
        deposit(t, -amount);
    }

    public synchronized boolean join(Transaction t) {
        if (currentTx != null) return false;
        currentTx = t;
        workingBalance = balance;
        return true;
    }

    public synchronized boolean canCommit(Transaction t) {
        return (t == currentTx);
    }

    public synchronized void abort(Transaction t) {
        if (t == currentTx)
            currentTx = null;
    }

    public synchronized void commit(Transaction t) throws Failure{
        if (t != currentTx) throw new Failure();
        balance = workingBalance;
        currentTx = null;
    }

}


class ProxyAccount /* implements TransBankAccount */ {
    private TransBankAccount delegate;

    public boolean join(Transaction t) {
        return delegate.join(t);
    }


    public long balance(Transaction t) throws Failure {
        return delegate.balance(t);
    }

    // and so on...

}


class FailedTransferException extends Exception {}
class RetryableTransferException extends Exception {}

class TransactionLogger {
    void cancelLogEntry(Transaction t, long amount,
                        TransBankAccount src, TransBankAccount dst) {}

    void logTransfer(Transaction t, long amount,
                     TransBankAccount src, TransBankAccount dst) {}

    void logCompletedTransfer(Transaction t, long amount,
                              TransBankAccount src, TransBankAccount dst) {}

}


class AccountUser {
    TransactionLogger log;                // a made-up class

    // helper method called on any failure
    void rollback(Transaction t, long amount,
                  TransBankAccount src, TransBankAccount dst) {
        log.cancelLogEntry(t, amount, src, dst);
        src.abort(t);
        dst.abort(t);
    }
    public boolean transfer(long amount,
                            TransBankAccount src,
                            TransBankAccount dst)
            throws FailedTransferException, RetryableTransferException {

        if (src == null || dst == null)        // screen arguments
            throw new IllegalArgumentException();
        if (src == dst) return true;           // avoid aliasing

        Transaction t = new Transaction();
        log.logTransfer(t, amount, src, dst);  // record

        if (!src.join(t) || !dst.join(t)) {    // cannot join
            rollback(t, amount, src, dst);
            throw new RetryableTransferException();
        }

        try {
            src.withdraw(t, amount);
            dst.deposit(t, amount);
        }
        catch (InsufficientFunds ex) {         // semantic failure
            rollback(t, amount, src, dst);
            return false;
        }
        catch (Failure k) {                    // transaction error
            rollback(t, amount, src, dst);
            throw new RetryableTransferException();
        }

        if (!src.canCommit(t) || !dst.canCommit(t)) { // interference
            rollback(t, amount, src, dst);
            throw new RetryableTransferException();
        }

        try {
            src.commit(t);
            dst.commit(t);
            log.logCompletedTransfer(t, amount, src, dst);
            return true;
        }
        catch(Failure k) {                    // commitment failure
            rollback(t, amount, src, dst);
            throw new FailedTransferException();
        }

    }
}

class ColoredThing {

    protected Color myColor = Color.red; // the sample property
    protected boolean changePending;

    // vetoable listeners:
    protected final VetoableChangeMulticaster vetoers =
            new VetoableChangeMulticaster(this);

    // also some ordinary listeners:
    protected final PropertyChangeMulticaster listeners =
            new PropertyChangeMulticaster(this);

    // registration methods, including:
    void addVetoer(VetoableChangeListener l) {
        vetoers.addVetoableChangeListener(l);
    }

    public synchronized Color getColor() { // property accessor
        return myColor;
    }

    // internal helper methods
    protected synchronized void commitColor(Color newColor) {
        myColor = newColor;
        changePending = false;
    }

    protected synchronized void abortSetColor() {
        changePending = false;
    }

    public void setColor(Color newColor)
            throws PropertyVetoException {
        Color oldColor = null;
        boolean completed = false;

        synchronized (this) {

            if (changePending) { // allow only one transaction at a time
                throw new PropertyVetoException(
                        "Concurrent modification", null);
            }
            else if (newColor == null) {   // Argument screening
                throw new PropertyVetoException(
                        "Cannot change color to Null", null);
            }
            else {
                changePending = true;
                oldColor = myColor;
            }
        }

        try {
            vetoers.fireVetoableChange("color", oldColor, newColor);
            // fall through if no exception:
            commitColor(newColor);
            completed = true;
            // notify other listeners that change is committed
            listeners.firePropertyChange("color", oldColor, newColor);
        }
        catch(PropertyVetoException ex) { // abort on veto
            abortSetColor();
            completed = true;
            throw ex;
        }
        finally {                    // trap any unchecked exception
            if (!completed) abortSetColor();
        }
    }
}

class Semaphore implements Sync  {

    protected long permits; // current number of available permits

    public Semaphore(long initialPermits) {
        permits = initialPermits;
    }

    public synchronized void release() {
        ++permits;
        notify();
    }



    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();
        synchronized(this) {
            try {
                while (permits <= 0) wait();
                --permits;
            }
            catch (InterruptedException ie) {
                notify();
                throw ie;
            }
        }
    }

    public boolean attempt(long msecs)throws InterruptedException{
        if (Thread.interrupted()) throw new InterruptedException();
        synchronized(this) {
            if (permits > 0) {     // Same as acquire but messier
                --permits;
                return true;
            }
            else if (msecs <= 0)   // avoid timed wait if not needed
                return false;
            else {
                try {
                    long startTime = System.currentTimeMillis();
                    long waitTime = msecs;

                    for (;;) {
                        wait(waitTime);
                        if (permits > 0) {
                            --permits;
                            return true;
                        }
                        else {                   // Check for time-out
                            long now = System.currentTimeMillis();
                            waitTime = msecs - (now - startTime);
                            if (waitTime <= 0)
                                return false;
                        }
                    }
                }
                catch(InterruptedException ie) {
                    notify();
                    throw ie;
                }
            }
        }
    }
}

final class BoundedBufferWithDelegates {
    private Object[] array;
    private Exchanger putter;
    private Exchanger taker;

    public BoundedBufferWithDelegates(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0) throw new IllegalArgumentException();
        array = new Object[capacity];
        putter = new Exchanger(capacity);
        taker = new Exchanger(0);
    }


    public void put(Object x) throws InterruptedException {
        putter.exchange(x);
    }

    public Object take() throws InterruptedException {
        return taker.exchange(null);
    }

    void removedSlotNotification(Exchanger h) { // relay
        if (h == putter)  taker.addedSlotNotification();
        else             putter.addedSlotNotification();
    }

    protected class Exchanger {                 // Inner class
        protected int ptr = 0;          // circular index
        protected int slots;            // number of usable slots
        protected int waiting = 0;      // number of waiting threads

        Exchanger(int n) { slots = n; }

        synchronized void addedSlotNotification() {
            ++slots;
            if (waiting > 0) // unblock a single waiting thread
                notify();
        }

        Object exchange(Object x) throws InterruptedException {
            Object old = null; // return value

            synchronized(this) {
                while (slots <= 0) { // wait for slot
                    ++waiting;
                    try {
                        wait();
                    }
                    catch(InterruptedException ie) {
                        notify();
                        throw ie;
                    }
                    finally {
                        --waiting;
                    }
                }

                --slots;             // use slot
                old = array[ptr];
                array[ptr] = x;
                ptr = (ptr + 1) % array.length; // advance position
            }

            removedSlotNotification(this); // notify of change
            return old;
        }
    }
}


final class BoundedBufferWithMonitorObjects {
    private final Object[] array;   // the elements

    private int putPtr = 0;         // circular indices
    private int takePtr = 0;

    private int emptySlots;         // slot counts
    private int usedSlots = 0;

    private int waitingPuts = 0;    // counts of waiting threads
    private int waitingTakes = 0;

    private final Object putMonitor = new Object();
    private final Object takeMonitor = new Object();

    public BoundedBufferWithMonitorObjects(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0)
            throw new IllegalArgumentException();

        array = new Object[capacity];
        emptySlots = capacity;
    }



    public void put(Object x) throws InterruptedException {
        synchronized(putMonitor) {
            while (emptySlots <= 0) {
                ++waitingPuts;
                try { putMonitor.wait(); }
                catch(InterruptedException ie) {
                    putMonitor.notify();
                    throw ie;
                }
                finally { --waitingPuts; }
            }
            --emptySlots;
            array[putPtr] = x;
            putPtr = (putPtr + 1) % array.length;
        }
        synchronized(takeMonitor) { // directly notify
            ++usedSlots;
            if (waitingTakes > 0)
                takeMonitor.notify();
        }
    }

    public Object take() throws InterruptedException {
        Object old = null;
        synchronized(takeMonitor) {
            while (usedSlots <= 0) {
                ++waitingTakes;
                try { takeMonitor.wait(); }
                catch(InterruptedException ie) {
                    takeMonitor.notify();
                    throw ie;
                }
                finally { --waitingTakes; }
            }
            --usedSlots;
            old = array[takePtr];
            array[takePtr] = null;
            takePtr = (takePtr + 1) % array.length;
        }
        synchronized(putMonitor) {
            ++emptySlots;
            if (waitingPuts > 0)
                putMonitor.notify();
        }
        return old;
    }

}

class FIFOSemaphore extends Semaphore {

    protected final WaitQueue queue = new WaitQueue();

    public FIFOSemaphore(long initialPermits) {
        super(initialPermits);
    }

    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();

        WaitNode node = null;

        synchronized(this) {
            if (permits > 0) {    // no need to queue
                --permits;
                return;
            }
            else {
                node = new WaitNode();
                queue.enq(node);
            }
        }

        // must release lock before node wait

        node.doWait();

    }

    public synchronized void release() {
        for (;;) {                // retry until success
            WaitNode node = queue.deq();

            if (node == null) {    // queue is empty
                ++permits;
                return;
            }
            else if (node.doNotify())
                return;

            // else node was already released due to
            //   interruption or time-out, so must retry
        }
    }


    // Queue node class. Each node serves as a monitor.

    protected static class WaitNode {
        boolean released = false;
        WaitNode next = null;

        synchronized void doWait() throws InterruptedException {
            try {
                while (!released)
                    wait();
            }
            catch (InterruptedException ie) {

                if (!released) {        // Interrupted before notified
                    // Suppress future notifications:
                    released = true;
                    throw ie;
                }
                else {                  // Interrupted after notified
                    // Ignore exception but propagate status:
                    Thread.currentThread().interrupt();
                }

            }
        }

        synchronized boolean doNotify() { // return true if notified

            if (released)             // was interrupted or timed out
                return false;
            else {
                released = true;
                notify();
                return true;
            }
        }

        synchronized boolean doTimedWait(long msecs)
                throws InterruptedException {
            return true;
            // similar
        }
    }

    // Standard linked queue class.
    // Used only when holding Semaphore lock.

    protected static class WaitQueue {
        protected WaitNode head = null;
        protected WaitNode last = null;

        protected void enq(WaitNode node) {
            if (last == null)
                head = last = node;
            else {
                last.next = node;
                last = node;
            }
        }

        protected WaitNode deq() {
            WaitNode node = head;
            if (node != null) {
                head = node.next;
                if (head == null) last = null;
                node.next = null;
            }
            return node;
        }
    }
}

class WebService implements Runnable {
    static final int PORT = 1040;   // just for demo
    Handler handler = new Handler();

    public void run() {
        try {
            ServerSocket socket = new ServerSocket(PORT);
            for (;;) {
                final Socket connection = socket.accept();
                new Thread(new Runnable() {
                    public void run() {
                        handler.process(connection);
                    }}).start();
            }
        }
        catch(Exception e) { } // die
    }

    public static void main(String[] args) {
        new Thread(new WebService()).start();
    }
}
class Handler {

    void process(Socket s) {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
            int request = in.readInt();
            int result = -request;     // return negation to client
            out.writeInt(result);
        }
        catch(IOException ex) {}     // fall through

        finally {                    // clean up
            try { if (in != null) in.close(); }
            catch (IOException ignore) {}
            try { if (out != null) out.close(); }
            catch (IOException ignore) {}
            try  { s.close(); }
            catch (IOException ignore) {}
        }
    }

}



class OpenCallHost {                     // Generic code sketch
    protected long localState;
    protected final Helper helper = new Helper();

    protected synchronized void updateState() {
        localState = 2; // ...;
    }

    public void req() {
        updateState();
        helper.handle();
    }
}

class ThreadPerMessageHost {                // Generic code sketch
    protected long localState;
    protected final Helper helper = new Helper();

    protected synchronized void updateState() {
        localState = 2; // ...;
    }

    public void req() {
        updateState();
        new Thread(new Runnable() {
            public void run() {
                helper.handle();
            }
        }).start();
    }
}

interface Executor {
    void execute(Runnable r);
}

class HostWithExecutor {                 // Generic code sketch
    protected long localState;
    protected final Helper helper = new Helper();
    protected final Executor executor;

    public HostWithExecutor(Executor e) { executor = e; }

    protected synchronized void updateState() {
        localState = 2; // ...;
    }

    public void req() {
        updateState();
        executor.execute(new Runnable() {
            public void run() {
                helper.handle();
            }
        });
    }
}

class PlainWorkerPool implements Executor {
    protected final Channel workQueue;

    public void execute(Runnable r) {
        try {
            workQueue.put(r);
        }
        catch (InterruptedException ie) { // postpone response
            Thread.currentThread().interrupt();
        }
    }

    public PlainWorkerPool(Channel ch, int nworkers) {
        workQueue = ch;
        for (int i = 0; i < nworkers; ++i) activate();
    }

    protected void activate() {
        Runnable runLoop = new Runnable() {
            public void run() {
                try {
                    for (;;) {
                        Runnable r = (Runnable)(workQueue.take());
                        r.run();
                    }
                }
                catch (InterruptedException ie) {} // die
            }
        };
        new Thread(runLoop).start();
    }
}

class TimerDaemon {                               // Fragments

    static class TimerTask implements Comparable {
        final Runnable command;
        final long execTime;       // time to run at
        public int compareTo(Object x) {
            long otherExecTime = ((TimerTask)(x)).execTime;
            return (execTime < otherExecTime) ? -1 :
                    (execTime == otherExecTime)? 0 : 1;
        }

        TimerTask(Runnable r, long t) { command = r; execTime = t; }
    }

    // a heap or list with methods that preserve
    // ordering with respect to TimerTask.compareTo

    static class PriorityQueue {
        void put(TimerTask t) {}
        TimerTask least() { return null; }
        void removeLeast() {}
        boolean isEmpty() { return true; }
    }

    protected final PriorityQueue pq = new PriorityQueue();

    public synchronized void executeAfterDelay(Runnable r,long t){
        pq.put(new TimerTask(r, t + System.currentTimeMillis()));
        notifyAll();
    }
    public synchronized void executeAt(Runnable r, Date time) {
        pq.put(new TimerTask(r, time.getTime()));
        notifyAll();
    }

    // wait for and then return next task to run
    protected synchronized Runnable take()
            throws InterruptedException {
        for (;;) {
            while (pq.isEmpty())
                wait();
            TimerTask t = pq.least();
            long now = System.currentTimeMillis();
            long waitTime = now - t.execTime;
            if (waitTime <= 0) {
                pq.removeLeast();
                return t.command;
            }
            else
                wait(waitTime);
        }
    }

    public TimerDaemon() { activate(); } // only one

    void activate() {
        // same as PlainWorkerThread except using above take method
    }
}

class SessionTask implements Runnable {  // generic code sketch
    static final int BUFFSIZE = 1024;

    protected final Socket socket;
    protected final InputStream input;
    SessionTask(Socket s) throws IOException {
        socket = s; input = socket.getInputStream();
    }

    void processCommand(byte[] b, int n) {}

    void cleanup() {}

    public void run() {            // Normally run in a new thread
        byte[] commandBuffer = new byte[BUFFSIZE];
        try {
            for (;;) {
                int bytes = input.read(commandBuffer, 0, BUFFSIZE);
                if (bytes != BUFFSIZE) break;
                processCommand(commandBuffer, bytes);
            }
        }
        catch (IOException ex) {
            cleanup();
        }
        finally {
            try { input.close(); socket.close(); }
            catch(IOException ignore) {}
        }
    }
}
class IOEventTask implements Runnable {  // generic code sketch

    static final int BUFFSIZE = 1024;

    protected final Socket socket;
    protected final InputStream input;
    protected volatile boolean done = false; // latches true

    IOEventTask(Socket s) throws IOException {
        socket = s; input = socket.getInputStream();
    }


    void processCommand(byte[] b, int n) {}

    void cleanup() {}

    public void run() { // trigger only when input available
        if (done) return;

        byte[] commandBuffer = new byte[BUFFSIZE];
        try {
            int bytes = input.read(commandBuffer, 0, BUFFSIZE);
            if (bytes != BUFFSIZE) done = true;
            else processCommand(commandBuffer, bytes);
        }
        catch (IOException ex) {
            cleanup();
            done = true;
        }
        finally {
            if (!done) return;
            try { input.close(); socket.close(); }
            catch(IOException ignore) {}
        }
    }

    // Accessor methods needed by triggering agent:
    boolean done()     { return done; }
    InputStream input() { return input; }
}


class PollingWorker implements Runnable {       // Incomplete
    private java.util.List tasks = new LinkedList(); // ...;
    private long sleepTime = 100; // ...;

    void register(IOEventTask t)   { tasks.add(t); }
    void deregister(IOEventTask t) { tasks.remove(t); }

    public void run() {
        try {
            for (;;) {
                for (Iterator it = tasks.iterator(); it.hasNext();) {
                    IOEventTask t = (IOEventTask)(it.next());
                    if (t.done())
                        deregister(t);
                    else {
                        boolean trigger;
                        try {
                            trigger = t.input().available() > 0;
                        }
                        catch (IOException ex) {
                            trigger = true; // trigger if exception on check
                        }
                        if (trigger)
                            t.run();
                    }
                }
                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException ie) {}
    }
}


abstract class Box {
    protected Color color = Color.white;

    public synchronized Color getColor()        { return color; }
    public synchronized void  setColor(Color c) { color = c; }
    public abstract java.awt.Dimension size();
    public abstract Box duplicate();                 // clone
    public abstract void show(Graphics g, Point origin);// display
}
class BasicBox extends Box {
    protected Dimension size;

    public BasicBox(int xdim, int ydim) {
        size = new Dimension(xdim, ydim);
    }

    public synchronized Dimension size() { return size; }

    public void show(Graphics g, Point origin) {
        g.setColor(getColor());
        g.fillRect(origin.x, origin.y, size.width, size.height);
    }

    public synchronized Box duplicate() {
        Box p =  new BasicBox(size.width, size.height);
        p.setColor(getColor());
        return p;
    }
}

abstract class JoinedPair extends Box {
    protected Box fst; // one of the boxes
    protected Box snd; // the other one

    protected JoinedPair(Box a, Box b) {
        fst = a;
        snd = b;
    }

    public synchronized void flip() { // swap fst/snd
        Box tmp = fst; fst = snd; snd = tmp;
    }

    public void show(Graphics g, Point p) {}

    public Dimension size() { return new Dimension(0,0); }

    public Box duplicate() { return null; }

    //  other internal helper methods
}

class HorizontallyJoinedPair extends JoinedPair {

    public HorizontallyJoinedPair(Box l, Box r) {
        super(l, r);
    }

    public synchronized Box duplicate() {
        HorizontallyJoinedPair p =
                new HorizontallyJoinedPair(fst.duplicate(),
                        snd.duplicate());
        p.setColor(getColor());
        return p;
    }


    // ... other implementations of abstract Box methods
}

class VerticallyJoinedPair extends JoinedPair {

    public VerticallyJoinedPair(Box l, Box r) {
        super(l, r);
    }
    // similar
}

class WrappedBox extends Box {
    protected Dimension wrapperSize;
    protected Box inner;

    public WrappedBox(Box innerBox, Dimension size) {
        inner = innerBox;
        wrapperSize = size;
    }

    public void show(Graphics g, Point p) {}

    public Dimension size() { return new Dimension(0,0); }

    public Box duplicate() { return null; }

    // ... other implementations of abstract Box methods
}

interface PushSource {
    void start();
}
interface PushStage {
    void putA(Box p);
}
interface DualInputPushStage extends PushStage {
    void putB(Box p);
}
class DualInputAdapter implements PushStage {
    protected final DualInputPushStage stage;

    public DualInputAdapter(DualInputPushStage s) { stage = s; }

    public void putA(Box p) { stage.putB(p); }

}
class DevNull implements PushStage {
    public void putA(Box p) { }
}
class SingleOutputPushStage {
    private PushStage next1 = null;
    protected synchronized PushStage next1() { return next1; }
    public synchronized void attach1(PushStage s) { next1 = s; }
}
class DualOutputPushStage extends SingleOutputPushStage {
    private PushStage next2 = null;
    protected synchronized PushStage next2() { return next2; }
    public synchronized void attach2(PushStage s) { next2 = s; }
}class Painter extends SingleOutputPushStage
        implements PushStage {
    protected final Color color; // the color to paint things

    public Painter(Color c) { color = c; }

    public void putA(Box p) {
        p.setColor(color);
        next1().putA(p);
    }
}

class Wrapper extends SingleOutputPushStage
        implements PushStage {
    protected final int thickness;

    public Wrapper(int t) { thickness = t; }

    public void putA(Box p) {
        Dimension d = new Dimension(thickness, thickness);
        next1().putA(new WrappedBox(p, d));
    }
}
class Flipper extends SingleOutputPushStage
        implements PushStage {
    public void putA(Box p) {
        if (p instanceof JoinedPair)
            ((JoinedPair) p).flip();
        next1().putA(p);
    }
}
abstract class Joiner extends SingleOutputPushStage
        implements DualInputPushStage {
    protected Box a = null;  // incoming from putA
    protected Box b = null;  // incoming from putB

    protected abstract Box join(Box p, Box q);

    protected synchronized Box joinFromA(Box p) {
        while (a != null)             // wait until last consumed
            try { wait(); }
            catch (InterruptedException e) { return null; }
        a = p;
        return tryJoin();
    }
    protected synchronized Box joinFromB(Box p) { // symmetrical
        while (b != null)
            try { wait(); }
            catch (InterruptedException ie) { return null; }
        b = p;
        return tryJoin();
    }

    protected synchronized Box tryJoin() {
        if (a == null || b == null) return null; // cannot join
        Box joined = join(a, b);              // make combined box
        a = b = null;                         // forget old boxes
        notifyAll();                          // allow new puts
        return joined;
    }

    public void putA(Box p) {
        Box j = joinFromA(p);
        if (j != null) next1().putA(j);
    }

    public void putB(Box p) {
        Box j = joinFromB(p);
        if (j != null) next1().putA(j);
    }
}
class HorizontalJoiner extends Joiner {
    protected Box join(Box p, Box q) {
        return new HorizontallyJoinedPair(p, q);
    }
}
class VerticalJoiner extends Joiner {
    protected Box join(Box p, Box q) {
        return new VerticallyJoinedPair(p, q);
    }
}

class Collector extends SingleOutputPushStage
        implements DualInputPushStage {
    public void putA(Box p) { next1().putA(p);}
    public void putB(Box p) { next1().putA(p); }
}
class Alternator extends DualOutputPushStage
        implements PushStage {
    protected boolean outTo2 = false; // control alternation

    protected synchronized boolean testAndInvert() {
        boolean b = outTo2;
        outTo2 = !outTo2;
        return b;
    }

    public  void putA(final Box p) {
        if (testAndInvert())
            next1().putA(p);
        else {
            new Thread(new Runnable() {
                public void run() { next2().putA(p); }
            }).start();
        }
    }
}
class Cloner extends DualOutputPushStage
        implements PushStage {

    public void putA(Box p) {
        final Box p2 = p.duplicate();
        next1().putA(p);
        new Thread(new Runnable() {
            public void run() { next2().putA(p2); }
        }).start();
    }

}
interface BoxPredicate {
    boolean test(Box p);
}

class MaxSizePredicate implements BoxPredicate {

    protected final int max; // max size to let through

    public MaxSizePredicate(int maximum) { max = maximum; }

    public boolean test(Box p) {
        return p.size().height <= max && p.size().width <= max;
    }
}

class Screener extends DualOutputPushStage
        implements PushStage {

    protected final BoxPredicate predicate;
    public Screener(BoxPredicate p) { predicate = p; }

    public void putA(final Box p) {
        if (predicate.test(p)) {
            new Thread(new Runnable() {
                public void run() { next1().putA(p); }
            }).start();
        }
        else
            next2().putA(p);
    }
}

class BasicBoxSource extends SingleOutputPushStage
        implements PushSource, Runnable {

    protected final Dimension size;     // maximum sizes
    protected final int productionTime; // simulated delay

    public BasicBoxSource(Dimension s, int delay) {
        size = s;
        productionTime = delay;
    }

    protected Box produce() {
        return new BasicBox((int)(Math.random() * size.width) + 1,
                (int)(Math.random() * size.height) + 1);
    }

    public void start() {
        next1().putA(produce());
    }

    public void run() {
        try {
            for (;;) {
                start();
                Thread.sleep((int)(Math.random() * 2* productionTime));
            }
        }
        catch (InterruptedException ie) { } // die
    }

}

interface FileReader {
    void read(String filename, FileReaderClient client);
}

interface FileReaderClient {
    void readCompleted(String filename, byte[] data);
    void readFailed(String filename, IOException ex);
}
class FileReaderApp implements FileReaderClient { // Fragments
    protected FileReader reader = new AFileReader();

    public void readCompleted(String filename, byte[] data) {
        // ... use data ...
    }

    public void readFailed(String filename, IOException ex){
        // ... deal with failure ...
    }

    public void actionRequiringFile() {
        reader.read("AppFile", this);
    }

    public void actionNotRequiringFile() { }
}

class AFileReader implements FileReader {

    public void read(final String fn, final FileReaderClient c) {
        new Thread(new Runnable() {
            public void run() { doRead(fn, c); }
        }).start();
    }

    protected void doRead(String fn, FileReaderClient client) {
        byte[] buffer = new byte[1024]; // just for illustration
        try {
            FileInputStream s = new FileInputStream(fn);
            s.read(buffer);
            if (client != null) client.readCompleted(fn, buffer);
        }
        catch (IOException ex) {
            if (client != null) client.readFailed(fn, ex);
        }
    }
}

class FileApplication implements FileReaderClient {
    private String[] filenames;
    private int currentCompletion; // index of ready file

    public synchronized void readCompleted(String fn, byte[] d) {
        // wait until ready to process this callback
        while (!fn.equals(filenames[currentCompletion])) {
            try { wait(); }
            catch(InterruptedException ex) { return; }
        }
        // ... process data...
        // wake up any other thread waiting on this condition:
        ++currentCompletion;
        notifyAll();
    }

    public synchronized void readFailed(String fn, IOException e){
        // similar...
    }

    public synchronized void readfiles() {
        AFileReader reader = new AFileReader();
        currentCompletion = 0;
        for (int i = 0; i < filenames.length; ++i)
            reader.read(filenames[i],this);
    }
}

interface Pic {
    byte[] getImage();
}

interface Renderer {
    Pic render(URL src);
}

class StandardRenderer implements Renderer {
    public Pic render(URL src) { return null ; }
}

class PictureApp {                       // Code sketch
    // ...
    private final Renderer renderer = new StandardRenderer();

    void displayBorders() {}
    void displayCaption() {}
    void displayImage(byte[] b) {}
    void cleanup() {}

    public void show(final URL imageSource) {

        class Waiter implements Runnable {
            private Pic result = null;
            Pic getResult() { return result; }
            public void run() {
                result = renderer.render(imageSource);
            }
        };

        Waiter waiter = new Waiter();
        Thread t = new Thread(waiter);
        t.start();

        displayBorders();  // do other things
        displayCaption();  //  while rendering

        try {
            t.join();
        }
        catch(InterruptedException e) {
            cleanup();
            return;
        }

        Pic pic = waiter.getResult();
        if (pic != null)
            displayImage(pic.getImage());
        else {}
        // ... deal with assumed rendering failure
    }
}

class AsynchRenderer implements Renderer {
    private final Renderer renderer = new StandardRenderer();

    static class FuturePic implements Pic { // inner class
        private Pic pic = null;
        private boolean ready = false;
        synchronized void setPic(Pic p) {
            pic = p;
            ready = true;
            notifyAll();
        }

        public synchronized byte[] getImage() {
            while (!ready)
                try { wait(); }
                catch (InterruptedException e) { return null; }
            return pic.getImage();
        }
    }

    public Pic render(final URL src) {
        final FuturePic p = new FuturePic();
        new Thread(new Runnable() {
            public void run() { p.setPic(renderer.render(src)); }
        }).start();
        return p;
    }
}
class PicturAppWithFuture {                   // Code sketch
    private final Renderer renderer = new AsynchRenderer();

    void displayBorders() {}
    void displayCaption() {}
    void displayImage(byte[] b) {}
    void cleanup() {}

    public void show(final URL imageSource) {
        Pic pic = renderer.render(imageSource);

        displayBorders();  // do other things ...
        displayCaption();

        byte[] im = pic.getImage();
        if (im != null)
            displayImage(im);
        else  {} // deal with assumed rendering failure
    }
}

class FutureResult {                            // Fragments
    protected Object value = null;
    protected boolean ready = false;
    protected InvocationTargetException exception = null;

    public synchronized Object get()
            throws InterruptedException, InvocationTargetException {

        while (!ready) wait();

        if (exception != null)
            throw exception;
        else
            return value;
    }

    public Runnable setter(final Callable function) {
        return new Runnable() {
            public void run() {
                try {
                    set(function.call());
                }
                catch(Throwable e) {
                    setException(e);
                }
            }
        };
    }

    synchronized void set(Object result) {
        value = result;
        ready = true;
        notifyAll();
    }

    synchronized void setException(Throwable e) {
        exception = new InvocationTargetException(e);
        ready = true;
        notifyAll();
    }

    // ... other auxiliary and convenience methods ...

}

class PictureDisplayWithFutureResult {            // Code sketch

    void displayBorders() {}
    void displayCaption() {}
    void displayImage(byte[] b) {}
    void cleanup() {}


    private final Renderer renderer = new StandardRenderer();
    // ...

    public void show(final URL imageSource) {

        try {
            FutureResult futurePic = new FutureResult();
            Runnable command = futurePic.setter(new Callable() {
                public Object call() {
                    return renderer.render(imageSource);
                }
            });
            new Thread(command).start();

            displayBorders();
            displayCaption();

            displayImage(((Pic)(futurePic.get())).getImage());
        }

        catch (InterruptedException ex) {
            cleanup();
            return;
        }
        catch (InvocationTargetException ex) {
            cleanup();
            return;
        }
    }
}


interface Disk {
    void read(int cylinderNumber, byte[] buffer) throws Failure;
    void write(int cylinderNumber, byte[] buffer) throws Failure;
}

abstract class DiskTask implements Runnable {
    protected final int cylinder;        // read/write parameters
    protected final byte[] buffer;
    protected Failure exception = null;       // to relay out
    protected DiskTask next = null;           // for use in queue
    protected final Latch done = new Latch(); // status indicator

    DiskTask(int c, byte[] b) { cylinder = c; buffer = b; }

    abstract void access() throws Failure; // read or write

    public void run() {
        try  { access(); }
        catch (Failure ex) { setException(ex); }
        finally { done.release(); }
    }

    void awaitCompletion() throws InterruptedException {
        done.acquire();
    }

    synchronized Failure getException() { return exception; }
    synchronized void setException(Failure f) { exception = f; }
}

class DiskReadTask extends DiskTask {
    DiskReadTask(int c, byte[] b) { super(c, b); }
    void access() throws Failure { /* ... raw read ... */ }
}

class DiskWriteTask extends DiskTask {
    DiskWriteTask(int c, byte[] b) { super(c, b); }
    void access() throws Failure { /* ... raw write ... */ }
}
class ScheduledDisk implements Disk {
    protected final DiskTaskQueue tasks = new DiskTaskQueue();

    public void read(int c, byte[] b) throws Failure {
        readOrWrite(new DiskReadTask(c, b));
    }

    public void write(int c, byte[] b) throws Failure {
        readOrWrite(new DiskWriteTask(c, b));
    }

    protected void readOrWrite(DiskTask t) throws Failure {
        tasks.put(t);
        try {
            t.awaitCompletion();
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // propagate
            throw new Failure(); // convert to failure exception
        }
        Failure f = t.getException();
        if (f != null) throw f;
    }

    public ScheduledDisk() {     // construct worker thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (;;) {
                        tasks.take().run();
                    }
                }
                catch (InterruptedException ex) {} // die
            }
        }).start();
    }
}

class DiskTaskQueue {
    protected DiskTask thisSweep = null;
    protected DiskTask nextSweep = null;
    protected int currentCylinder = 0;

    protected final Semaphore available = new Semaphore(0);

    void put(DiskTask t) {
        insert(t);
        available.release();
    }

    DiskTask take() throws InterruptedException {
        available.acquire();
        return extract();
    }

    synchronized void insert(DiskTask t) {
        DiskTask q;
        if (t.cylinder >= currentCylinder) {   // determine queue
            q = thisSweep;
            if (q == null) { thisSweep = t; return; }
        }
        else {
            q = nextSweep;
            if (q == null) { nextSweep = t; return; }
        }
        DiskTask trail = q;            // ordered linked list insert
        q = trail.next;
        for (;;) {
            if (q == null || t.cylinder < q.cylinder) {
                trail.next = t; t.next = q;
                return;
            }
            else {
                trail = q; q = q.next;
            }
        }
    }
    synchronized DiskTask extract() { // PRE: not empty
        if (thisSweep == null) {           // possibly swap queues
            thisSweep = nextSweep;
            nextSweep = null;
        }
        DiskTask t = thisSweep;
        thisSweep = t.next;
        currentCylinder = t.cylinder;
        return t;
    }
}

class Fib extends FJTask {
    static final int sequentialThreshold = 13; // for tuning
    volatile int number;                       // argument/result

    Fib(int n) { number = n; }

    int seqFib(int n) {
        if (n <= 1)
            return n;
        else
            return seqFib(n-1) + seqFib(n-2);
    }

    int getAnswer() {
        if (!isDone())
            throw new IllegalStateException("Not yet computed");
        return number;
    }

    public void run() {
        int n = number;

        if (n <= sequentialThreshold)       // base case
            number = seqFib(n);
        else {
            Fib f1 = new Fib(n - 1);          // create subtasks
            Fib f2 = new Fib(n - 2);

            coInvoke(f1, f2);                 // fork then join both

            number = f1.number + f2.number;   // combine results
        }
    }


    public static void main(String[] args) { // sample driver
        try {
            int groupSize = 2;    // 2 worker threads
            int num = 35;         // compute fib(35)
            FJTaskRunnerGroup group = new FJTaskRunnerGroup(groupSize);
            Fib f = new Fib(num);
            group.invoke(f);
            int result = f.getAnswer();
            System.out.println("Answer: " + result);
        }
        catch (InterruptedException ex) {} // die
    }
}


class FibVL extends FJTask {
    static final int sequentialThreshold = 13; // for tuning

    volatile int number; // as before
    final FibVL next;    // embedded linked list of sibling tasks

    FibVL(int n, FibVL list) { number = n; next = list; }
    int seqFib(int n) {
        if (n <= 1)
            return n;
        else
            return seqFib(n-1) + seqFib(n-2);
    }

    public void run() {
        int n = number;
        if (n <= sequentialThreshold)
            number = seqFib(n);
        else {
            FibVL forked = null;               // list of subtasks

            forked = new FibVL(n - 1, forked); // prepends to list
            forked.fork();

            forked = new FibVL(n - 2, forked);
            forked.fork();

            number = accumulate(forked);
        }
    }

    // Traverse list, joining each subtask and adding to result
    int accumulate(FibVL list) {
        int r = 0;
        for (FibVL f = list; f != null; f = f.next) {
            f.join();
            r += f.number;
        }
        return r;
    }
}

class FibVCB extends FJTask {
    static final int sequentialThreshold = 13; // for tuning

    // ...
    volatile int number = 0;        // as before
    final FibVCB parent;            // Is null for outermost call
    int callbacksExpected = 0;
    volatile int callbacksReceived = 0;

    FibVCB(int n, FibVCB p) { number = n; parent = p; }

    int seqFib(int n) {
        if (n <= 1)
            return n;
        else
            return seqFib(n-1) + seqFib(n-2);
    }

    // Callback method invoked by subtasks upon completion
    synchronized void addToResult(int n) {
        number += n;
        ++callbacksReceived;
    }

    public void run() {  // same structure as join-based version
        int n = number;
        if (n <= sequentialThreshold)
            number = seqFib(n);
        else {
            // clear number so subtasks can fill in
            number = 0;
            // establish number of callbacks expected
            callbacksExpected = 2;

            new FibVCB(n - 1, this).fork();
            new FibVCB(n - 2, this).fork();

            // Wait for callbacks from children
            while (callbacksReceived < callbacksExpected) yield();
        }

        // Call back parent
        if (parent != null) parent.addToResult(number);
    }
}


class NQueens extends FJTask {
    static int boardSize; // fixed after initialization in main
    // Boards are arrays where each cell represents a row,
    // and holds the column number of the queen in that row

    static class Result {          // holder for ultimate result
        private int[] board = null;  // non-null when solved

        synchronized boolean solved() { return board != null; }

        synchronized void set(int[] b) { // Support use by non-Tasks
            if (board == null) { board = b; notifyAll(); }
        }

        synchronized int[] await() throws InterruptedException {
            while (board == null) wait();
            return board;
        }
    }
    static final Result result = new Result();

    public static void main(String[] args) {
        try {
            boardSize = 8; // ...;
            FJTaskRunnerGroup tasks = new FJTaskRunnerGroup(4);
            int[] initialBoard = new int[0]; // start with empty board
            tasks.execute(new NQueens(initialBoard));
            int[] board = result.await();
        }
        catch (InterruptedException ie) {}
        // ...
    }

    final int[] sofar;            // initial configuration

    NQueens(int[] board) { this.sofar = board;  }

    public void run() {
        if (!result.solved()) {     // skip if already solved
            int row = sofar.length;

            if (row >= boardSize)     // done
                result.set(sofar);

            else {                    // try all expansions

                for (int q = 0; q < boardSize; ++q) {

                    // Check if queen can be placed in column q of next row
                    boolean attacked = false;
                    for (int i = 0; i < row; ++i) {
                        int p = sofar[i];
                        if (q == p || q == p - (row-i) || q == p + (row-i)) {
                            attacked = true;
                            break;
                        }
                    }

                    // If so, fork to explore moves from new configuration
                    if (!attacked) {
                        // build extended board representation
                        int[] next = new int[row+1];
                        for (int k = 0; k < row; ++k) next[k] = sofar[k];
                        next[row] = q;
                        new NQueens(next).fork();
                    }
                }
            }
        }
    }
}

abstract class JTree extends FJTask {
    volatile double maxDiff; // for convergence check
}

class Interior extends JTree {
    private final JTree[] quads;

    Interior(JTree q1, JTree q2, JTree q3, JTree q4) {
        quads = new JTree[] { q1, q2, q3, q4 };
    }

    public void run() {
        coInvoke(quads);
        double md = 0.0;
        for (int i = 0; i < quads.length; ++i) {
            md = Math.max(md,quads[i].maxDiff);
            quads[i].reset();
        }
        maxDiff = md;
    }
}

class Leaf extends JTree {
    private final double[][] A; private final double[][] B;
    private final int loRow;    private final int hiRow;
    private final int loCol;    private final int hiCol;
    private int steps = 0;

    Leaf(double[][] A, double[][] B,
         int loRow, int hiRow, int loCol, int hiCol) {
        this.A = A;   this.B = B;
        this.loRow = loRow; this.hiRow = hiRow;
        this.loCol = loCol; this.hiCol = hiCol;
    }

    public synchronized void run() {
        boolean AtoB = (steps++ % 2) == 0;
        double[][] a = (AtoB)? A : B;
        double[][] b = (AtoB)? B : A;
        double md = 0.0;
        for (int i = loRow; i <= hiRow; ++i) {
            for (int j = loCol; j <= hiCol; ++j) {
                b[i][j] = 0.25 * (a[i-1][j] + a[i][j-1] +
                        a[i+1][j] + a[i][j+1]);
                md = Math.max(md, Math.abs(b[i][j] - a[i][j]));
            }
        }
        maxDiff = md;
    }
}

class Jacobi extends FJTask {
    static final double EPSILON = 0.001; // convergence criterion
    final JTree root;
    final int maxSteps;
    Jacobi(double[][] A, double[][] B,
           int firstRow, int lastRow, int firstCol, int lastCol,
           int maxSteps, int leafCells) {
        this.maxSteps = maxSteps;
        root = build(A, B, firstRow, lastRow, firstCol, lastCol,
                leafCells);
    }

    public void run() {
        for (int i = 0; i < maxSteps; ++i) {
            invoke(root);
            if (root.maxDiff < EPSILON) {
                System.out.println("Converged");
                return;
            }
            else root.reset();
        }
    }

    static JTree build(double[][] a, double[][] b,
                       int lr, int hr, int lc, int hc, int size) {
        if ((hr - lr + 1) *  (hc - lc + 1) <= size)
            return new Leaf(a, b, lr, hr, lc, hc);
        int mr = (lr + hr) / 2; // midpoints
        int mc = (lc + hc) / 2;
        return new Interior(build(a, b, lr,   mr, lc,   mc, size),
                build(a, b, lr,   mr, mc+1, hc, size),
                build(a, b, mr+1, hr, lc,   mc, size),
                build(a, b, mr+1, hr, mc+1, hc, size));
    }
}


class CyclicBarrier {

    protected final int parties;
    protected int count;     // parties currently being waited for
    protected int resets = 0;  // times barrier has been tripped

    CyclicBarrier(int c) { count = parties = c; }

    synchronized int barrier() throws InterruptedException {
        int index = --count;
        if (index > 0) {        // not yet tripped
            int r = resets;       // wait until next reset

            do { wait(); } while (resets == r);

        }
        else {                 // trip
            count = parties;     // reset count for next time
            ++resets;
            notifyAll();         // cause all other parties to resume
        }

        return index;
    }
}

class Segment implements Runnable  {            // Code sketch
    final CyclicBarrier bar; // shared by all segments
    Segment(CyclicBarrier b) { bar = b; }

    void update() {  }

    public void run() {
        // ...
        try {
            for (int i = 0; i < 10 /* iterations */; ++i) {
                update();
                bar.barrier();
            }
        }
        catch (InterruptedException ie) {}
        // ...
    }
}

class Problem { int size; }

class Driver {
    // ...
    int granularity = 1;
    void compute(Problem problem) throws Exception {
        int n = problem.size / granularity;
        CyclicBarrier barrier = new CyclicBarrier(n);
        Thread[] threads = new Thread[n];

        // create
        for (int i = 0; i < n; ++i)
            threads[i] = new Thread(new Segment(barrier));

        // trigger
        for (int i = 0; i < n; ++i) threads[i].start();

        // await termination
        for (int i = 0; i < n; ++i) threads[i].join();
    }
}


class JacobiSegment implements Runnable {        // Incomplete
    // These are same as in Leaf class version:
    static final double EPSILON = 0.001;
    double[][] A;        double[][] B;
    final int firstRow;  final int lastRow;
    final int firstCol;  final int lastCol;
    volatile double maxDiff;
    int steps = 0;
    void update() { /* Nearly same as Leaf.run */ }

    final CyclicBarrier bar;
    final JacobiSegment[] allSegments; // needed for convergence check
    volatile boolean converged = false;

    JacobiSegment(double[][] A, double[][] B,
                  int firstRow, int lastRow,
                  int firstCol, int lastCol,
                  CyclicBarrier b, JacobiSegment[] allSegments) {
        this.A = A;   this.B = B;
        this.firstRow = firstRow; this.lastRow = lastRow;
        this.firstCol = firstCol; this.lastCol = lastCol;
        this.bar = b;
        this.allSegments = allSegments;
    }

    public void run() {
        try {
            while (!converged) {
                update();
                int myIndex = bar.barrier(); // wait for all to update
                if (myIndex == 0) convergenceCheck();
                bar.barrier();             // wait for convergence check
            }
        }
        catch(Exception ex) {
            // clean up ...
        }
    }

    void convergenceCheck() {
        for (int i = 0; i < allSegments.length; ++i)
            if (allSegments[i].maxDiff > EPSILON) return;
        for (int i = 0; i < allSegments.length; ++i)
            allSegments[i].converged = true;
    }
}



class ActiveRunnableExecutor extends Thread {
    Channel me = null; // ... //  used for all incoming messages

    public void run() {
        try {
            for (;;) {
                ((Runnable)(me.take())).run();
            }
        }
        catch (InterruptedException ie) {} // die
    }
}

/*
//import jcsp.lang.*;

class Fork implements jcsp.lang.CSProcess {

    private final jcsp.lang.AltingChannelInput[] fromPhil;

    Fork(jcsp.lang.AltingChannelInput l, jcsp.lang.AltingChannelInput r) {
        fromPhil = new jcsp.lang.AltingChannelInput[] { l, r };
    }

    public void run() {
        jcsp.lang.Alternative alt = new jcsp.lang.Alternative(fromPhil);

        for (;;) {
            int i = alt.select();   // await message from either
            fromPhil[i].read();     // pick up
            fromPhil[i].read();     // put down
        }

    }
}


class Butler implements jcsp.lang.CSProcess {

    private final jcsp.lang.AltingChannelInput[] enters;
    private final jcsp.lang.AltingChannelInput[] exits;

    Butler(jcsp.lang.AltingChannelInput[] e, jcsp.lang.AltingChannelInput[] x) {
        enters = e;
        exits = x;
    }

    public void run() {
        int seats = enters.length;
        int nseated = 0;

        // set up arrays for select
        jcsp.lang.AltingChannelInput[] chans = new jcsp.lang.AltingChannelInput[2*seats];
        for (int i = 0; i < seats; ++i) {
            chans[i] = exits[i];
            chans[seats + i] = enters[i];
        }

        jcsp.lang.Alternative either = new jcsp.lang.Alternative(chans);
        jcsp.lang.Alternative exit = new jcsp.lang.Alternative(exits);

        for (;;) {
            // if max number are seated, only allow exits
            jcsp.lang.Alternative alt = (nseated <  seats-1)? either : exit;

            int i = alt.fairSelect();
            chans[i].read();

            // if i is in first half of array, it is an exit message
            if (i < seats) --nseated; else ++nseated;
        }
    }
}

class Philosopher implements jcsp.lang.CSProcess {

    private final jcsp.lang.ChannelOutput leftFork;
    private final jcsp.lang.ChannelOutput rightFork;
    private final jcsp.lang.ChannelOutput enter;
    private final jcsp.lang.ChannelOutput exit;

    Philosopher(jcsp.lang.ChannelOutput l, jcsp.lang.ChannelOutput r,
                jcsp.lang.ChannelOutput e, jcsp.lang.ChannelOutput x) {
        leftFork = l;
        rightFork = r;
        enter = e;
        exit = x;
    }

    public void run() {

        for (;;) {

            think();

            enter.write(null);          // get seat
            leftFork.write(null);       // pick up left
            rightFork.write(null);      // pick up right

            eat();

            leftFork.write(null);       // put down left
            rightFork.write(null);      // put down right
            exit.write(null);           // leave seat

        }

    }

    private void eat() {}
    private void think() {}

}

class College implements jcsp.lang.CSProcess {
    final static int N = 5;

    private final jcsp.lang.CSProcess action;

    College() {
        jcsp.lang.One2OneChannel[] lefts = jcsp.lang.One2OneChannel.create(N);
        jcsp.lang.One2OneChannel[] rights = jcsp.lang.One2OneChannel.create(N);
        jcsp.lang.One2OneChannel[] enters = jcsp.lang.One2OneChannel.create(N);
        jcsp.lang.One2OneChannel[] exits = jcsp.lang.One2OneChannel.create(N);

        Butler butler = new Butler(enters, exits);

        Philosopher[] phils = new Philosopher[N];
        for (int i = 0; i < N; ++i)
            phils[i] = new Philosopher(lefts[i], rights[i],
                    enters[i], exits[i]);

        Fork[] forks = new Fork[N];
        for (int i = 0; i < N; ++i)
            forks[i] = new Fork(rights[(i + 1) % N], lefts[i]);

        action = new jcsp.lang.Parallel(
                new jcsp.lang.CSProcess[] {
                        butler,
                        new jcsp.lang.Parallel(phils),
                        new jcsp.lang.Parallel(forks)
                });
    }

    public void run() { action.run(); }

    public static void main(String[] args) {
        new College().run();
    }


}*/


public class CPJ {
   /* public static void main(String[] args) {
        System.out.println(1);
    }*/
}
