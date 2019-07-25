package com.packagename.myapp.spring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseContent {
    private int count, id;

    void setId(int id){
        this.id = id;
    }

    int getId() {return id;}

    int getId(int id) {return id;}

    int getCount(String table, Connection con){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + table);
            resultSet.next();
            count = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}

class person extends DataBaseContent{
    private String name, surname, patronymic;

    person(){}

    person(String name, String surname, String patronymic){
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    void setName(String name){
        this.name = name;
    }

    void setSurname(String surname){
        this.surname = surname;
    }

    void setPatronymic(String patronymic){
        this.patronymic = patronymic;
    }

    String getName(){
        return name;
    }

    String getName(String table, int num, Connection con){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM " + table + " WHERE ID = " + num);
            resultSet.next();
            name = resultSet.getString("name");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    String getSurname(){
        return surname;
    }

    String getSurname(String table, int num, Connection con){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT surname FROM " + table + " WHERE ID = " + num);
            resultSet.next();
            surname = resultSet.getString("surname");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return surname;
    }

    String getPatronymic(){
        return patronymic;
    }

    String getPatronymic(String table, int num, Connection con){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT patronymic FROM " + table + " WHERE ID = " + num);
            resultSet.next();
            patronymic = resultSet.getString("patronymic");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return patronymic;
    }
}

class patient extends person{
    private int phone;

    patient(){ }

    patient(int id, String name, String surname, String patronymic, int phone){
        setId(id);
        setName(name);
        setSurname(surname);
        setPatronymic(patronymic);
        setPhone(phone);
    }

    String getName(int num, Connection con){
        return super.getName("patient", num, con);
    }

    String getSurname(int num, Connection con){
        return super.getSurname("patient", num, con);
    }

    String getPatronymic(int num, Connection con){
        return super.getPatronymic("patient", num, con);
    }

    int getPhone(){return phone;}

    int getPhone(int num, Connection con){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT phone FROM patient WHERE ID = " + num);
            resultSet.next();
            phone = resultSet.getInt("phone");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phone;
    }

    int getCount(Connection con){
        return super.getCount("patient", con);
    }

    void setPhone(int phone){
        this.phone = phone;
    }
}

class doctor extends person{
    private String specialization;

    doctor(){}

    doctor(int id, String name, String surname, String patronymic, String specialization){
        setId(id);
        setName(name);
        setSurname(surname);
        setPatronymic(patronymic);
        setSpecialization(specialization);
    }

    String getName(int num, Connection con){
        return super.getName("doctor", num, con);
    }

    String getSurname(int num, Connection con){
        return super.getSurname("doctor", num, con);
    }

    String getPatronymic(int num, Connection con){
        return super.getPatronymic("doctor", num, con);
    }

    void setSpecialization(String specialization){
        this.specialization = specialization;
    }

    String getSpecialization(){
        return specialization;
    }

    String getSpecialization(int num, Connection con){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT specialization FROM doctor WHERE ID = " + num);
            resultSet.next();
            specialization = resultSet.getString("specialization");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialization;
    }

    int getCount(Connection con){
        return super.getCount("doctor", con);
    }
}

class recipe extends DataBaseContent{
    private String description;
    private String pat;
    private String doc;
    private String creationDate;
    private String validity;
    private String priority;

    recipe(){}

    recipe(int id, String description, String pat, String doc, String creationDate, String validity, String priority){
        setId(id);
        setDescription(description);
        setPat(pat);
        setDoc(doc);
        setCreationDate(creationDate);
        setValidity(validity);
        setPriority(priority);
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setPat(String pat) {
        this.pat = pat;
    }

    void setDoc(String doc) {
        this.doc = doc;
    }

    void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    void setValidity(String validity) {
        this.validity = validity;
    }

    void setPriority(String priority) {
        this.priority = priority;
    }

    String getDescription() {
        return description;
    }

    String getDescription(int num, Connection con) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT description FROM recipe WHERE ID = " + num);
            resultSet.next();
            description = resultSet.getString("description");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return description;
    }

    String getPat() {
        return pat;
    }

    String getPat(int num, Connection con) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT patient FROM recipe WHERE ID = " + num);
            resultSet.next();
            pat = resultSet.getString("patient");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pat;
    }

    String getDoc() {
        return doc;
    }

    String getDoc(int num, Connection con) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT doctor FROM recipe WHERE ID = " + num);
            resultSet.next();
            doc = resultSet.getString("doctor");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doc;
    }

    String getCreationDate() {
        return creationDate;
    }

    String getCreationDate(int num, Connection con) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT creationDate FROM recipe WHERE ID = " + num);
            resultSet.next();
            creationDate = resultSet.getString("creationDate");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creationDate;
    }

    String getValidity() {
        return validity;
    }

    String getValidity(int num, Connection con) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT validity FROM recipe WHERE ID = " + num);
            resultSet.next();
            validity = resultSet.getString("validity");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return validity;
    }

    String getPriority() {
        return priority;
    }

    String getPriority(int num, Connection con) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT priority FROM recipe WHERE ID = " + num);
            resultSet.next();
            priority = resultSet.getString("priority");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priority;
    }

    int getCount(Connection con){
        return super.getCount("recipe", con);
    }
}