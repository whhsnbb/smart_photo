package com.example.common.Bean;

public class UserInfo {

    private int status;
    private Data data;



    public class Data{
        private int uid;
        private String phone;
        private String username;
        private String portrait; // 头像文件
        private String gender;  //性别
        private String birth;
        private int age;
        private String introduce;  //个性签名

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAge() {
            return age;
        }

        public String getBirth() {
            return birth;
        }

        public String getGender() {
            return gender;
        }

        public String getIntroduce() {
            return introduce;
        }

        public String getPortrait() {
            return portrait;
        }

        public String getUsername() {
            return username;
        }

        public int getUid() {
            return uid;
        }

        public String getPhone() {
            return phone;
        }
    }


    public void setData(Data data) {
        this.data = data;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }


}
