package com.guozi.lambda;

import java.util.function.Supplier;

/**
 * @author guozi
 * @date 2018-05-15 14:47
 */
public class ConstructorReference {

    public static void main(String[] args) {
        //构造器引用
        //根据参数列表自动匹配构造器
        Supplier<ConstructorReference> sup = ConstructorReference::new;
        ConstructorReference c1 = sup.get();
        ConstructorReference c2 = sup.get();

        System.out.println("----"+c1);
        System.out.println("----"+c2);

        System.out.println(c1.equals(c2));
        System.out.println("----------------  分割线   ---------------------------------");

        Supplier<TestBean> supplier = TestBean::new;
        TestBean testBean1 = supplier.get();
        TestBean testBean2 = supplier.get();

        System.out.println("----"+testBean1);
        System.out.println("----"+testBean2);

        System.out.println(testBean1.equals(testBean2));

    }

}
