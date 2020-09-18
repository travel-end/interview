package com.journey.interview.test;

/**
 * @By Journey 2020/9/18
 * @Description 构建者模式
 */
public class People {
    //'必选参数'
    private String name;
    //'以下都是可选参数'
    private int gender;
    private int age;
    private int height;
    private int weight;

    //'私有构造函数，限制必须通过构造者构建对象'
    private People(Builder builder) {
        this.name = builder.name;
        this.gender = builder.gender;
        this.age = builder.age;
        this.height = builder.height;
        this.weight = builder.weight;
    }

    //'构造者'
    public static class Builder {
        private String name;
        private int gender;
        private int age;
        private int height;
        private int weight;

        //'必选参数必须在构造函数中传入'
        public Builder(String name) {
            this.name = name;
        }

        //'以下是每个非必要属性的设值函数，它返回构造者本身用于链式调用'
        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder gender(int gender) {
            this.gender = gender;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        //'构建对象'
        public People build() {
            return new People(this);
        }
    }
}
