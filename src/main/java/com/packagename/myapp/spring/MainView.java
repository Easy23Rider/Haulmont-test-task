package com.packagename.myapp.spring;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DialogWindow extends Dialog{
    private int height = 260, width = 400;

    private TextField id;
    private TextField name, surname, patronymic, phone, specialization;
    private TextField description;
    private ComboBox comboBoxPatient, comboBoxDoctor, priority;
    private TextField statRecipeCount;
    private DatePicker creationDate, validity;

    Dialog dialog = new Dialog();

    Button buttonOk = new Button("Ok");
    Button buttonCancel = new Button("Отмена");

    DialogWindow(String type, String table){
        dialog.add(this.id = new TextField("Номер"));
        this.id.setEnabled(false);
        switch (type){
            case "add" :
            case "edit" : {
                switch (table){
                    case "patient" : {
                        dialog.add(this.name = new TextField("Имя"));
                        dialog.add(this.surname = new TextField("Фамилия"));
                        dialog.add(this.patronymic = new TextField("Отчество"));
                        dialog.add(this.phone = new TextField("Телефон"));

                        dialog.setHeight(height + "px");
                        dialog.setWidth(width + "px");
                    } break;
                    case "doctor" : {
                        dialog.add(this.name = new TextField("Имя"));
                        dialog.add(this.surname = new TextField("Фамилия"));
                        dialog.add(this.patronymic = new TextField("Отчество"));
                        dialog.add(this.specialization = new TextField("Специальность"));

                        dialog.setHeight(height + "px");
                        dialog.setWidth(width + "px");
                    } break;
                    case "recipe" : {
                        dialog.add(this.description = new TextField("Описание"));
                        dialog.add(this.comboBoxPatient = new ComboBox("Пациент"));
                        dialog.add(this.comboBoxDoctor = new ComboBox("Доктор"));
                        dialog.add(this.creationDate = new DatePicker("Дата создания"));
                        dialog.add(this.validity = new DatePicker("Срок действия"));
                        dialog.add(this.priority = new ComboBox("Приоритет"));

                        creationDate.setLocale(Locale.CANADA);
                        validity.setLocale(Locale.CANADA);

                        priority.setItems("Normal", "Cito", "Statim");

                        dialog.setHeight(height + 100 + "px");
                        dialog.setWidth(width + "px");
                    } break;
                }
            } break;
            case "delete" : {
                VerticalLayout layout = new VerticalLayout();
                layout.add(id, new Label("Удалить выбранную запись?"));
                dialog.add(layout);
                dialog.setHeight(height - 40  + "px");
                dialog.setWidth(width - 40 + "px");
            } break;
            case "stat" : {
                VerticalLayout layout = new VerticalLayout();
                HorizontalLayout layout1Name = new HorizontalLayout();
                this.name = new TextField("Имя");
                name.setEnabled(false);
                this.surname = new TextField("Фамилия");
                surname.setEnabled(false);
                this.patronymic = new TextField("Отчество");
                patronymic.setEnabled(false);
                layout1Name.add(name, surname, patronymic);
                this.statRecipeCount = new TextField();
                statRecipeCount.setEnabled(false);

                layout.add(id, new Label("Доктор"), layout1Name, new Label("Выписал(а) рецептов:"), statRecipeCount);
                dialog.add(layout);

                dialog.setHeight(height + 120 + "px");
                dialog.setWidth(width + 220 + "px");

                buttonOk.addClickListener(event -> dialog.close());
            } break;
        }

        dialog.add(buttonOk);

        dialog.add(buttonCancel);
        buttonCancel.addClickListener(event -> dialog.close());
    }

    String getID() {return id.getValue();}

    String getName(){
        return name.getValue();
    }

    String getSurname(){
        return surname.getValue();
    }

    String getPatronymic() {return patronymic.getValue();}

    String getPhone() {return phone.getValue();}

    String getSpecialization(){return specialization.getValue();}

    String getDescription() {return description.getValue();}

    LocalDate getCreationDate() {return creationDate.getValue();}

    LocalDate getValidity() {return validity.getValue();}

    String getPriority() {return String.valueOf(priority.getValue());}

    String getComboBoxPatient() {return String.valueOf(comboBoxPatient.getValue());}

    String getComboBoxDoctor() {return String.valueOf(comboBoxDoctor.getValue());}

    String getStatRecipeCount() {return statRecipeCount.getValue();}

    void setID(String id) {this.id.setValue(id);}

    void setName(String name) {this.name.setValue(name);}

    void setSurname(String surname) {this.surname.setValue(surname);}

    void setPatronymic(String patronymic) {this.patronymic.setValue(patronymic);}

    void setPhone(String phone) {this.phone.setValue(phone);}

    void setSpecialization(String specialization) {this.specialization.setValue(specialization);}

    void setDescription(String description) {this.description.setValue(description);}

    void setCreationDate(LocalDate creationDate) {this.creationDate.setValue(creationDate);}

    void setValidity(LocalDate validity) {this.validity.setValue(validity);}

    void setPriority(String priority) {this.priority.setValue(priority);}

    void setComboBoxPatientValue(String patient) {this.comboBoxPatient.setValue(patient);}

    void setComboBoxDoctorValue(String doctor) {this.comboBoxDoctor.setValue(doctor);}

    void setComboBoxPatientItems(ArrayList<String> names) {this.comboBoxPatient.setItems(names);}

    void setComboBoxDoctorItems(ArrayList<String> names) {this.comboBoxDoctor.setItems(names);}

    void setStatRecipeCount(String statRecipeCount) {this.statRecipeCount.setValue(statRecipeCount);}
}

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout{

    void addColumnsPatients(Grid<patient> grid, List<patient> list){
        grid.removeAllColumns();
        grid.setItems(list);
        grid.addColumn(patient::getId).setHeader("Номер");
        grid.addColumn(patient::getName).setHeader("Имя");
        grid.addColumn(patient::getSurname).setHeader("Фамилия");
        grid.addColumn(patient::getPatronymic).setHeader("Отчество");
        grid.addColumn(patient::getPhone).setHeader("Телефон");
    }

    void addColumnsDoctors(Grid<doctor> grid, List<doctor> list){
        grid.removeAllColumns();
        grid.setItems(list);
        grid.addColumn(doctor::getId).setHeader("Номер");
        grid.addColumn(doctor::getName).setHeader("Имя");
        grid.addColumn(doctor::getSurname).setHeader("Фамилия");
        grid.addColumn(doctor::getPatronymic).setHeader("Отчество");
        grid.addColumn(doctor::getSpecialization).setHeader("Специализация");
    }

    void addColumnsRecipes(Grid<recipe> grid, List<recipe> list){
        grid.removeAllColumns();
        grid.setItems(list);
        grid.addColumn(recipe::getId).setHeader("Номер");
        grid.addColumn(recipe::getDescription).setHeader("Описание");
        grid.addColumn(recipe::getPat).setHeader("Пациент");
        grid.addColumn(recipe::getDoc).setHeader("Доктор");
        grid.addColumn(recipe::getCreationDate).setHeader("Дата создания");
        grid.addColumn(recipe::getValidity).setHeader("Срок действия");
        grid.addColumn(recipe::getPriority).setHeader("Приоритет");
    }

    void comboBoxGetNames(DataBaseConnection db, String table, DialogWindow dialogWindow){
        try {
            ArrayList<String> names = new ArrayList<String>();
            String sql = "SELECT name, surname, patronymic FROM " + table;
            ResultSet rs = db.connection.createStatement().executeQuery(sql);
            while(rs.next()){
                String currName = rs.getString("name") + " " + rs.getString("surname") + " " + rs.getString("patronymic");
                names.add(currName);
            }
            switch (table){
                case "patient": dialogWindow.setComboBoxPatientItems(names); break;
                case "doctor": dialogWindow.setComboBoxDoctorItems(names); break;
            }
        } catch (SQLException e) {}
    }

    public MainView() {

        DataBaseConnection db = new DataBaseConnection();
        if (!db.loadDriver()) return;
        if (!db.getConnection()) return;


        // Создание вкладки Пациенты и заполнение таблицы из бд
        Tab tabPatients = new Tab("Пациенты");
        patient p = new patient();
        List<patient> patientList = new ArrayList<>();

        for (int i = 1; i<=p.getCount(db.connection); i++) {
            patientList.add(new patient(p.getId(i), p.getName(i, db.connection), p.getSurname(i, db.connection), p.getPatronymic(i, db.connection), p.getPhone(i, db.connection)));
        }

        Grid<patient> gridPatients = new Grid<>(patient.class);
        addColumnsPatients(gridPatients, patientList);


        // Создание вкладки Доктора и заполнение таблицы из бд
        Tab tabDoctors = new Tab("Доктора");
        doctor d = new doctor();
        List<doctor> doctorList = new ArrayList<>();

        for (int i = 1; i<=d.getCount(db.connection); i++) {
            doctorList.add(new doctor(d.getId(i), d.getName(i, db.connection), d.getSurname(i, db.connection), d.getPatronymic(i, db.connection), d.getSpecialization(i, db.connection)));
        }

        Grid<doctor> gridDoctors = new Grid<>(doctor.class);
        addColumnsDoctors(gridDoctors, doctorList);
        gridDoctors.setVisible(false);


        // Создание вкладки Рецепты и заполнение таблицы из бд
        Tab tabRecipes = new Tab("Рецепты");
        recipe r = new recipe();
        List<recipe> recipeList = new ArrayList<>();

        for (int i = 1; i<=r.getCount(db.connection); i++) {
            recipeList.add(new recipe(r.getId(i), r.getDescription(i, db.connection), r.getPat(i, db.connection), r.getDoc(i, db.connection), r.getCreationDate(i, db.connection), r.getValidity(i, db.connection), r.getPriority(i, db.connection)));
        }

        Grid<recipe> gridRecipes = new Grid<>(recipe.class);
        addColumnsRecipes(gridRecipes, recipeList);
        gridRecipes.setVisible(false);


        // Связывание вкладок с таблицами
        Map<Tab, Component> tabsToGrids = new HashMap<>();
        tabsToGrids.put(tabPatients, gridPatients);
        tabsToGrids.put(tabDoctors, gridDoctors);
        tabsToGrids.put(tabRecipes, gridRecipes);
        Tabs tabs = new Tabs(tabPatients, tabDoctors, tabRecipes);
        Set<Component> gridsShown = Stream.of(gridPatients).collect(Collectors.toSet());


        // Создание диалогового окна Добавить пациента
        DialogWindow dialogAddPatient = new DialogWindow("add", "patient");

        // Добавление пациента при вводе данных нажатии кнопки Ок
        dialogAddPatient.buttonOk.addClickListener(event ->{
            try {
                int currNum = p.getCount(db.connection)+1;
                Statement statement = db.connection.createStatement();
                String sql = "INSERT INTO patient (" +
                        "ID, name, surname, patronymic, phone) VALUES('" + currNum + "',' " +
                        dialogAddPatient.getName() + "', '"+
                        dialogAddPatient.getSurname() + "', '" +
                        dialogAddPatient.getPatronymic() + "', '"+
                        dialogAddPatient.getPhone() + "')";
                statement.executeUpdate(sql);
                patientList.add(new patient(p.getId(currNum), p.getName(currNum, db.connection), p.getSurname(currNum, db.connection), p.getPatronymic(currNum, db.connection), p.getPhone(currNum, db.connection)));
                addColumnsPatients(gridPatients, patientList);
                gridPatients.deselectAll();
                dialogAddPatient.dialog.close();
            } catch (SQLException e) {}
        });


        // Создание диалогового окна Добавить доктора
        DialogWindow dialogAddDoctor = new DialogWindow("add", "doctor");

        // Добавление доктора при вводе данных нажатии кнопки Ок
        dialogAddDoctor.buttonOk.addClickListener(event ->{
            try {
                int currNum = d.getCount(db.connection)+1;
                Statement statement = db.connection.createStatement();
                String sql = "INSERT INTO doctor (" +
                        "ID, name, surname, patronymic, specialization) VALUES('" + currNum + "',' " +
                        dialogAddDoctor.getName() + "', '"+
                        dialogAddDoctor.getSurname() + "', '" +
                        dialogAddDoctor.getPatronymic() + "', '"+
                        dialogAddDoctor.getSpecialization() + "')";
                statement.executeUpdate(sql);
                doctorList.add(new doctor(d.getId(currNum), d.getName(currNum, db.connection), d.getSurname(currNum, db.connection), d.getPatronymic(currNum, db.connection), d.getSpecialization(currNum, db.connection)));
                addColumnsDoctors(gridDoctors, doctorList);
                gridDoctors.deselectAll();
                dialogAddDoctor.dialog.close();
            } catch (SQLException e) {}
        });


        // Создание диалогового окна Добавить рецепт
        DialogWindow dialogAddRecipe = new DialogWindow("add", "recipe");

        // Добавление рецепта при вводе данных нажатии кнопки Ок
        dialogAddRecipe.buttonOk.addClickListener(event ->{
            try {
                int currNum = r.getCount(db.connection)+1;
                Statement statement = db.connection.createStatement();
                String sql = "INSERT INTO recipe (" +
                        "ID, description, patient, doctor, creationDate, validity, priority) VALUES('" + currNum + "',' "+
                        dialogAddRecipe.getDescription() + "', '" +
                        dialogAddRecipe.getComboBoxPatient() + "', '" +
                        dialogAddRecipe.getComboBoxDoctor() + "', '" +
                        dialogAddRecipe.getCreationDate() + "', '" +
                        dialogAddRecipe.getValidity() + "', '" +
                        dialogAddRecipe.getPriority() + "')";
                statement.executeUpdate(sql);
                recipeList.add(new recipe(r.getId(currNum), r.getDescription(currNum, db.connection), r.getPat(currNum, db.connection), r.getDoc(currNum, db.connection), r.getCreationDate(currNum, db.connection),r.getValidity(currNum, db.connection), r.getPriority(currNum, db.connection)));
                addColumnsRecipes(gridRecipes, recipeList);
                gridRecipes.deselectAll();
                dialogAddRecipe.dialog.close();
            } catch (SQLException e) {}
        });

        // Создание диалогового окна Изменить пациента
        DialogWindow dialogEditPatient = new DialogWindow("edit", "patient");

        // Заполнение полей при открытии окна Изменить пациента
        gridPatients.addItemClickListener(selected -> {
            dialogEditPatient.setID(String.valueOf(selected.getItem().getId()));
            dialogEditPatient.setName(selected.getItem().getName());
            dialogEditPatient.setSurname(selected.getItem().getSurname());
            dialogEditPatient.setPatronymic(selected.getItem().getPatronymic());
            dialogEditPatient.setPhone(String.valueOf(selected.getItem().getPhone()));
        });

        // Изменение пациента при вводе данных нажатии кнопки Ок
        dialogEditPatient.buttonOk.addClickListener(event ->{
            try {
                Statement statement = db.connection.createStatement();
                String sql = "UPDATE patient SET " +
                        "name = '" + dialogEditPatient.getName() + "', " +
                        "surname = '" + dialogEditPatient.getSurname() + "', " +
                        "patronymic = '" + dialogEditPatient.getPatronymic() + "', " +
                        "phone = '" + dialogEditPatient.getPhone() + "' " +
                        "WHERE ID = " + dialogEditPatient.getID();
                statement.executeUpdate(sql);
                int currNum = Integer.parseInt(dialogEditPatient.getID());
                patientList.set(currNum-1, new patient(p.getId(currNum), p.getName(currNum, db.connection), p.getSurname(currNum, db.connection), p.getPatronymic(currNum, db.connection), p.getPhone(currNum, db.connection)));
                addColumnsPatients(gridPatients, patientList);
                dialogEditPatient.dialog.close();
            } catch (SQLException e) {}
        });


        // Создание диалогового окна Изменить доктора
        DialogWindow dialogEditDoctor = new DialogWindow("edit", "doctor");

        // Заполнение полей при открытии окна Изменить доктора
        gridDoctors.addItemClickListener(selected -> {
            dialogEditDoctor.setID(String.valueOf(selected.getItem().getId()));
            dialogEditDoctor.setName(selected.getItem().getName());
            dialogEditDoctor.setSurname(selected.getItem().getSurname());
            dialogEditDoctor.setPatronymic(selected.getItem().getPatronymic());
            dialogEditDoctor.setSpecialization(String.valueOf(selected.getItem().getSpecialization()));
        });

        // Изменение доктора при вводе данных нажатии кнопки Ок
        dialogEditDoctor.buttonOk.addClickListener(event ->{
            try {
                Statement statement = db.connection.createStatement();
                String sql = "UPDATE doctor SET " +
                        "name = '" + dialogEditDoctor.getName() + "', " +
                        "surname = '" + dialogEditDoctor.getSurname() + "', " +
                        "patronymic = '" + dialogEditDoctor.getPatronymic() + "', " +
                        "specialization = '" + dialogEditDoctor.getSpecialization() + "' " +
                        "WHERE ID = " + dialogEditDoctor.getID();
                statement.executeUpdate(sql);
                int currNum = Integer.parseInt(dialogEditDoctor.getID());
                doctorList.set(currNum-1, new doctor(d.getId(currNum), d.getName(currNum, db.connection), d.getSurname(currNum, db.connection), d.getPatronymic(currNum, db.connection), d.getSpecialization(currNum, db.connection)));
                addColumnsDoctors(gridDoctors, doctorList);
                dialogEditDoctor.dialog.close();
            } catch (SQLException e) {}
        });


        // Создание диалогового окна Изменить рецепт
        DialogWindow dialogEditRecipe = new DialogWindow("edit", "recipe");

        // Заполнение полей при открытии окна Изменить рецепт
        gridRecipes.addItemClickListener(selected -> {
            comboBoxGetNames(db, "patient", dialogEditRecipe);
            comboBoxGetNames(db, "doctor", dialogEditRecipe);

            dialogEditRecipe.setID(String.valueOf(selected.getItem().getId()));
            dialogEditRecipe.setDescription(selected.getItem().getDescription());
            dialogEditRecipe.setComboBoxPatientValue(selected.getItem().getPat());
            dialogEditRecipe.setComboBoxDoctorValue(selected.getItem().getDoc());
            dialogEditRecipe.setCreationDate(LocalDate.parse(selected.getItem().getCreationDate()));
            dialogEditRecipe.setValidity(LocalDate.parse(selected.getItem().getValidity()));
            dialogEditRecipe.setPriority(selected.getItem().getPriority());
        });

        // Изменение рецепта при вводе данных нажатии кнопки Ок
        dialogEditRecipe.buttonOk.addClickListener(event ->{
            try {
                Statement statement = db.connection.createStatement();
                String sql = "UPDATE recipe SET " +
                        "description = '" + dialogEditRecipe.getDescription() + "', " +
                        "patient = '" + dialogEditRecipe.getComboBoxPatient() + "', " +
                        "doctor = '" + dialogEditRecipe.getComboBoxDoctor() + "', " +
                        "creationDate = '" + dialogEditRecipe.getCreationDate() + "', " +
                        "validity = '" + dialogEditRecipe.getValidity() + "', " +
                        "priority = '" + dialogEditRecipe.getPriority() + "' " +
                        "WHERE ID = " + dialogEditRecipe.getID();
                statement.executeUpdate(sql);
                int currNum = Integer.parseInt(dialogEditRecipe.getID());
                recipeList.set(currNum-1, new recipe(r.getId(currNum), r.getDescription(currNum, db.connection), r.getPat(currNum, db.connection), r.getDoc(currNum, db.connection), r.getCreationDate(currNum, db.connection), r.getValidity(currNum, db.connection), r.getPriority(currNum, db.connection)));
                addColumnsRecipes(gridRecipes, recipeList);
                dialogEditRecipe.dialog.close();
            } catch (SQLException e) {}
        });


        // Создание диалогового окна Удалить пациента
        DialogWindow dialogDeletePatient = new DialogWindow("delete", "patient");

        // Заполнение полей при открытии окна Удалить пациента
        gridPatients.addItemClickListener(selected -> {
            dialogDeletePatient.setID(String.valueOf(selected.getItem().getId()));
        });

        // Удаление пациента при нажатии кнопки Ок
        dialogDeletePatient.buttonOk.addClickListener(event ->{
            try {
                Statement statement = db.connection.createStatement();
                String sql = "DELETE FROM patient WHERE id = '" + dialogDeletePatient.getID() + "'";
                statement.executeUpdate(sql);
                int currNum = Integer.parseInt(dialogEditPatient.getID())+1;
                for (int i = currNum; i<=p.getCount(db.connection)+1; i++) {
                    sql = "UPDATE patient SET ID = '" + (i - 1) +"' WHERE ID = '" + i + "'";
                    statement.executeUpdate(sql);
                }
                patientList.clear();
                for (int i = 1; i<=p.getCount(db.connection); i++) {
                    patientList.add(new patient(p.getId(i), p.getName(i, db.connection), p.getSurname(i, db.connection), p.getPatronymic(i, db.connection), p.getPhone(i, db.connection)));
                }
                addColumnsPatients(gridPatients, patientList);
                dialogDeletePatient.dialog.close();
            } catch (SQLException e) {}
        });

        // Создание диалогового окна Удалить доктора
        DialogWindow dialogDeleteDoctor = new DialogWindow("delete", "doctor");

        // Заполнение полей при открытии окна Удалить доктора
        gridDoctors.addItemClickListener(selected -> dialogDeleteDoctor.setID(String.valueOf(selected.getItem().getId())));

        // Удаление доктора при нажатии кнопки Ок
        dialogDeleteDoctor.buttonOk.addClickListener(event ->{
            try {
                Statement statement = db.connection.createStatement();
                String sql = "DELETE FROM doctor WHERE id = '" + dialogDeleteDoctor.getID() + "'";
                statement.executeUpdate(sql);
                int currNum = Integer.parseInt(dialogEditDoctor.getID())+1;
                for (int i = currNum; i<=d.getCount(db.connection)+1; i++) {
                    sql = "UPDATE doctor SET ID = '" + (i - 1) +"' WHERE ID = '" + i + "'";
                    statement.executeUpdate(sql);
                }
                doctorList.clear();
                for (int i = 1; i<=d.getCount(db.connection); i++) {
                    doctorList.add(new doctor(d.getId(i), d.getName(i, db.connection), d.getSurname(i, db.connection), d.getPatronymic(i, db.connection), d.getSpecialization(i, db.connection)));
                }
                addColumnsDoctors(gridDoctors, doctorList);
                dialogDeleteDoctor.dialog.close();
            } catch (SQLException e) {}
        });

        // Создание диалогового окна Удалить рецепт
        DialogWindow dialogDeleteRecipe = new DialogWindow("delete", "recipe");

        // Заполнение полей при открытии окна Удалить рецепт
        gridRecipes.addItemClickListener(selected -> dialogDeleteRecipe.setID(String.valueOf(selected.getItem().getId())));

        // Удаление рецепта при нажатии кнопки Ок
        dialogDeleteRecipe.buttonOk.addClickListener(event ->{
            try {
                Statement statement = db.connection.createStatement();
                String sql = "DELETE FROM recipe WHERE id = '" + dialogDeleteRecipe.getID() + "'";
                statement.executeUpdate(sql);
                int currNum = Integer.parseInt(dialogEditRecipe.getID())+1;
                for (int i = currNum; i<=r.getCount(db.connection)+1; i++) {
                    sql = "UPDATE recipe SET ID = '" + (i - 1) +"' WHERE ID = '" + i + "'";
                    statement.executeUpdate(sql);
                }
                recipeList.clear();
                for (int i = 1; i<=r.getCount(db.connection); i++) {
                    recipeList.add(new recipe(r.getId(i), r.getDescription(i, db.connection), r.getPat(i, db.connection), r.getDoc(i, db.connection), r.getCreationDate(i, db.connection), r.getValidity(i, db.connection), r.getPriority(i, db.connection)));
                }
                addColumnsRecipes(gridRecipes, recipeList);
                dialogDeleteRecipe.dialog.close();
            } catch (SQLException e) {}
        });


        // Создание диалогового окна Статистика доктора
        DialogWindow dialogDoctorStatistic = new DialogWindow("stat", "doctor");
        gridDoctors.addItemClickListener(selected -> {
            dialogDoctorStatistic.setID(String.valueOf(selected.getItem().getId()));
            dialogDoctorStatistic.setName(String.valueOf(selected.getItem().getName()));
            dialogDoctorStatistic.setSurname(String.valueOf(selected.getItem().getSurname()));
            dialogDoctorStatistic.setPatronymic(String.valueOf(selected.getItem().getPatronymic()));

            try {
                Statement statement = db.connection.createStatement();
                String sql = "SELECT COUNT(*) AS count FROM recipe WHERE " +
                        "doctor = '" + dialogDoctorStatistic.getName()+ " "+
                        dialogDoctorStatistic.getSurname() +" " +
                        dialogDoctorStatistic.getPatronymic() + "'";
                ResultSet rs = statement.executeQuery(sql);
                rs.next();
                dialogDoctorStatistic.setStatRecipeCount(String.valueOf(rs.getString("count")));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        // Создание кнопок на вкладке Пациенты
        HorizontalLayout layoutPatientButtons = new HorizontalLayout();
        Button buttonAddPatient = new Button("Добавить пациента");
        buttonAddPatient.addClickListener(event -> dialogAddPatient.dialog.open());

        Button buttonEditPatient = new Button("Изменить пациента");
        buttonEditPatient.addClickListener(event -> {
            dialogEditPatient.dialog.open();
            gridPatients.deselectAll();
        });
        buttonEditPatient.setEnabled(false);

        Button buttonDeletePatient = new Button("Удалить пациента");
        buttonDeletePatient.addClickListener(event -> {
            dialogDeletePatient.dialog.open();
            gridPatients.deselectAll();
        });
        buttonDeletePatient.setEnabled(false);
        layoutPatientButtons.add(buttonAddPatient, buttonEditPatient, buttonDeletePatient);


        //Создание кнопок на вкладке Доктора
        HorizontalLayout layoutDoctorButtons = new HorizontalLayout();
        Button buttonAddDoctor = new Button("Добавить доктора");
        buttonAddDoctor.addClickListener(event -> dialogAddDoctor.dialog.open());

        Button buttonEditDoctor = new Button("Изменить доктора");
        buttonEditDoctor.addClickListener(event -> {
            dialogEditDoctor.dialog.open();
            gridDoctors.deselectAll();
        });
        buttonEditDoctor.setEnabled(false);

        Button buttonDeleteDoctor = new Button("Удалить доктора");
        buttonDeleteDoctor.addClickListener(event -> {
            dialogDeleteDoctor.dialog.open();
            gridDoctors.deselectAll();
        });
        buttonDeleteDoctor.setEnabled(false);

        Button buttonDoctorStat = new Button("Статистика доктора");
        buttonDoctorStat.addClickListener(event -> {
            dialogDoctorStatistic.dialog.open();
            gridDoctors.deselectAll();
        });
        buttonDoctorStat.setEnabled(false);
        layoutDoctorButtons.add(buttonAddDoctor, buttonEditDoctor, buttonDeleteDoctor, buttonDoctorStat);
        layoutDoctorButtons.setVisible(false);


        //Создание кнопок на вкладке Рецепты
        HorizontalLayout layoutRecipeButtons = new HorizontalLayout();
        VerticalLayout layoutRecipeComponents = new VerticalLayout();
        Button buttonAddRecipe = new Button("Добавить рецепт");
        buttonAddRecipe.addClickListener(event -> {
            comboBoxGetNames(db, "patient", dialogAddRecipe);
            comboBoxGetNames(db, "doctor", dialogAddRecipe);
            dialogAddRecipe.dialog.open();
        });

        Button buttonEditRecipe = new Button("Изменить рецепт");
        buttonEditRecipe.addClickListener(event -> {
            dialogEditRecipe.dialog.open();
            gridRecipes.deselectAll();
        });
        buttonEditRecipe.setEnabled(false);

        Button buttonDeleteRecipe = new Button("Удалить рецепт");
        buttonDeleteRecipe.addClickListener(event -> {
            dialogDeleteRecipe.dialog.open();
            gridRecipes.deselectAll();
        });
        buttonDeleteRecipe.setEnabled(false);

        layoutRecipeButtons.add(buttonAddRecipe, buttonEditRecipe, buttonDeleteRecipe);
        layoutRecipeButtons.setVisible(false);


        // Создание фильтров
        VerticalLayout layoutRecipeFilters = new VerticalLayout();
        HorizontalLayout layoutRecipeFiltersFields = new HorizontalLayout();
        ListDataProvider<recipe> recipeDataProvider = new ListDataProvider<>(recipeList);
        gridRecipes.setDataProvider(recipeDataProvider);

        TextField filterDescription = new TextField("Описание");
        filterDescription.addValueChangeListener(event -> recipeDataProvider.addFilter(filter -> StringUtils.containsIgnoreCase(filter.getDescription(), filterDescription.getValue())));
        filterDescription.setValueChangeMode(ValueChangeMode.EAGER);

        TextField filterPatient = new TextField("Пациент");
        filterPatient.addValueChangeListener(event -> recipeDataProvider.addFilter(filter -> StringUtils.containsIgnoreCase(filter.getPat(), filterPatient.getValue())));
        filterPatient.setValueChangeMode(ValueChangeMode.EAGER);

        TextField filterPriority = new TextField("Приоритет");
        filterPriority.addValueChangeListener(event -> recipeDataProvider.addFilter(filter -> StringUtils.containsIgnoreCase(filter.getPriority(), filterPriority.getValue())));
        filterPriority.setValueChangeMode(ValueChangeMode.EAGER);


        layoutRecipeFiltersFields.add(filterDescription, filterPatient, filterPriority);
        layoutRecipeFilters.add(new Label("Фильтрация списка:"), layoutRecipeFiltersFields);
        layoutRecipeFilters.setVisible(false);

        // Переключение видимости элементов вкладок
        tabs.addSelectedChangeListener(event -> {
            gridsShown.forEach(grid -> {
                grid.setVisible(false);
                gridPatients.deselectAll();
                gridDoctors.deselectAll();
                gridRecipes.deselectAll();
                buttonEditPatient.setEnabled(false);
                buttonEditDoctor.setEnabled(false);
                buttonEditRecipe.setEnabled(false);
            });
            gridsShown.clear();
            Component selectedGrid = tabsToGrids.get(tabs.getSelectedTab());
            selectedGrid.setVisible(true);
            switch (tabs.getSelectedTab().getLabel()){
                case "Пациенты": {
                    layoutPatientButtons.setVisible(true);
                    layoutDoctorButtons.setVisible(false);
                    layoutRecipeButtons.setVisible(false);
                    layoutRecipeFilters.setVisible(false);
                    break;
                }
                case "Доктора": {
                    layoutPatientButtons.setVisible(false);
                    layoutDoctorButtons.setVisible(true);
                    layoutRecipeButtons.setVisible(false);
                    layoutRecipeFilters.setVisible(false);
                    break;
                }
                case "Рецепты": {
                    layoutPatientButtons.setVisible(false);
                    layoutDoctorButtons.setVisible(false);
                    layoutRecipeButtons.setVisible(true);
                    layoutRecipeFilters.setVisible(true);
                    break;
                }
            }
            gridsShown.add(selectedGrid);
        });


        // Выключение кнопок, если не выбрана строка таблицы
        gridPatients.addSelectionListener(event -> { if (!gridPatients.getSelectedItems().isEmpty()) {
            buttonEditPatient.setEnabled(true);
            buttonDeletePatient.setEnabled(true);
        } else {
            buttonEditPatient.setEnabled(false);
            buttonDeletePatient.setEnabled(false);
        }});

        gridDoctors.addSelectionListener(event -> { if (!gridDoctors.getSelectedItems().isEmpty()) {
            buttonEditDoctor.setEnabled(true);
            buttonDeleteDoctor.setEnabled(true);
            buttonDoctorStat.setEnabled(true);
        } else {
            buttonEditDoctor.setEnabled(false);
            buttonDeleteDoctor.setEnabled(false);
            buttonDoctorStat.setEnabled(false);
        }});

        gridRecipes.addSelectionListener(event -> {if (!gridRecipes.getSelectedItems().isEmpty()) {
            buttonEditRecipe.setEnabled(true);
            buttonDeleteRecipe.setEnabled(true);
        } else {
            buttonEditRecipe.setEnabled(false);
            buttonDeleteRecipe.setEnabled(false);
        }});

        // Добавление элементов
        add(tabs);
        add(layoutRecipeFilters);
        add(gridPatients);
        add(gridDoctors);
        add(gridRecipes);

        add(layoutPatientButtons);
        add(layoutDoctorButtons);
        add(layoutRecipeButtons);
    }

}
