package com.mars.java;

public class DynamicDispatch {
//     abstract class Human{
//        protected abstract void sayHello();
//    }
//     class Man extends Human{
//        @Override
//        protected void sayHello() {
//            System.out.println("Man hello");
//        }
//    }
//     class Woman extends Human{
//        @Override
//        protected void sayHello() {
//            System.out.println("woman say hello");
//        }
//    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woMan = new Woman();
        man.sayHello();
        woMan.sayHello();
        man = new Woman();
        man.sayHello();
    }
}
