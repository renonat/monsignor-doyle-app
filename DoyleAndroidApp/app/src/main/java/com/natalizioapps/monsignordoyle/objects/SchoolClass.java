package com.natalizioapps.monsignordoyle.objects;

/**
 * Created by Reno on 14-09-09.
 */
public class SchoolClass {
    //TODO: COMMENT
    private int _period;
    private String _name;
    private String _teacher;
    private String _room;
    private int _color;

    private SchoolClass(Builder builder) {
        this._period = builder._period;
        this._name = builder._name;
        this._color = builder._color;
        this._teacher = builder._teacher;
        this._room = builder._room;
    }

    public int getColor() {
        return _color;
    }

    public String getName() {
        return _name;
    }

    public int getPeriod() {
        return _period;
    }

    public String getTeacher() { return _teacher; }

    public String getRoom() { return _room; }


    public static class Builder {
        private int _period;
        private String _name;
        private int _color;
        private String _teacher;
        private String _room;

        public Builder period(int period) {
            this._period = period;
            return this;
        }

        public Builder name(String name) {
            this._name = name;
            return this;
        }

        public Builder color(int color) {
            this._color = color;
            return this;
        }

        public Builder teacher(String teacher) {
            this._teacher = teacher;
            return this;
        }

        public Builder room(String room) {
            this._room = room;
            return this;
        }

        public SchoolClass build() {
            return new SchoolClass(this);
        }

    }


}
