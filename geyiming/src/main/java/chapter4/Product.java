package chapter4;

/**
 * Created by lili on 2017/4/27.
 */
public final class Product {
    private final String no;
    private final String name;
    private final double price;

    /**
     * 不需要set方法
     * 对象创建完之后就不会变了
     * @param no
     * @param name
     * @param price
     */
    public Product(String no, String name, double price) {
        super();
        this.no = no;
        this.name = name;
        this.price = price;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
