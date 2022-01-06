package com.mars.multi.thread.learn.test.atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicReferenceTest {


    static AtomicReference<User> userAtomicReference = new AtomicReference<>();

    static AtomicMarkableReference<User> userAtomicMarkableReference = new AtomicMarkableReference<>(new User("w1", 2), false);
    static User user1 = new User("w1", 1);
    static AtomicStampedReference<User> userAtomicStampedReference = new AtomicStampedReference<User>(user1, 2);

    public static void main(String[] args) throws InterruptedException {
        User user = new User("w", 1);
        userAtomicReference.set(user);
        User updateUser = new User("s", 1);
        userAtomicReference.compareAndSet(user, updateUser);

//        System.out.println(userAtomicReference.get());
//        System.out.println(user);

        System.out.println(userAtomicMarkableReference.get(new boolean[]{false}));
        userAtomicMarkableReference.attemptMark(updateUser, true);
        System.out.println(userAtomicMarkableReference.getReference());


        System.out.println(userAtomicStampedReference.getReference());
        userAtomicStampedReference.compareAndSet(user1, new User("s1", 2), 2, 3);

        System.out.println(userAtomicStampedReference.getReference());
        System.out.println(user1);
    }


    static class User {
        private String name;
        private int old;

        public User(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOld() {
            return old;
        }

        public void setOld(int old) {
            this.old = old;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", old=" + old +
                    '}';
        }
    }

    static class User2 {
        private String name;
        private int old;

        public User2(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOld() {
            return old;
        }

        public void setOld(int old) {
            this.old = old;
        }

        @Override
        public String toString() {
            return "User2{" +
                    "name='" + name + '\'' +
                    ", old=" + old +
                    '}';
        }
    }
}
