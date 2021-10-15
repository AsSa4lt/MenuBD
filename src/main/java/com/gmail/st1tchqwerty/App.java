package com.gmail.st1tchqwerty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import javax.swing.text.View;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("MenuTest");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add food");
                    System.out.println("2: view food");
                    System.out.println("3: view by parameters");
                    System.out.println("4: choose a set of dishes so that their total weight is no more than 1 kg");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            AddFood(sc);
                            return;
                        case "2":
                            ViewAll();
                            return;
                        case "3":
                            ViewBy(sc);
                            return;
                        case "4":
                            SetOfDishes();
                            return;
                        case "5":
                            AddData();
                            return;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void Menu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("1: add food");
        System.out.println("2: view food");
        System.out.println("3: view by parameters");
        System.out.println("4: choose a set of dishes so that their total weight is no more than 1 kg");
        System.out.print("-> ");

        String s = sc.nextLine();
        switch (s) {
            case "1":
                AddFood(sc);
                return;
            case "2":
                ViewAll();
                return;
            case "3":
                ViewBy(sc);
                return;
            case "4":
                SetOfDishes();
                return;
            case "5":
                AddData();
                return;
            default:
                return;
        }
    }

    private static void AddData(){
        em.getTransaction().begin();
        MenuObj dish1 = new MenuObj("Caesar salad", 120.20, 300, false);
        MenuObj dish2 = new MenuObj("Greek salad", 99.99, 250, true);
        MenuObj dish3 = new MenuObj("Spring salad", 56.50, 250, true);
        MenuObj dish4 = new MenuObj("Salmon salad", 200.50, 200, false);
        MenuObj dish5 = new MenuObj("Tuna salad", 250.99, 150, true);
        em.persist(dish1);
        em.persist(dish2);
        em.persist(dish3);
        em.persist(dish4);
        em.persist(dish5);
        em.getTransaction().commit();
        Menu();
    }


    private static void SetOfDishes(){
        Query query = em.createQuery(
                "SELECT c FROM MenuObj c", MenuObj.class);
        List<MenuObj> list = (List<MenuObj>) query.getResultList();
        double sumMenu = 0;
        for (MenuObj m : list) {
            sumMenu += m.getWeight();
            if(sumMenu > 1000){
                break;
            }
            System.out.println(m.toString());
        }
        Menu();
    }

    private static void ViewAll() {
        Query query = em.createQuery(
                "SELECT c FROM MenuObj c", MenuObj.class);
        List<MenuObj> list = (List<MenuObj>) query.getResultList();

        for (MenuObj c : list)
            System.out.println(c);
        Menu();
    }

    private static void ViewBy(Scanner sc) {
        Query query = em.createQuery(
                "SELECT c FROM MenuObj c", MenuObj.class);
        List<MenuObj> list = (List<MenuObj>) query.getResultList();

        System.out.print("Enter food discount(Y/N): ");
        String sDiscount = sc.nextLine();
        boolean discount = false;
        if(sDiscount=="Y"){
            discount=true;
        }
        if(sDiscount=="N"){
            discount=false;
        }

        System.out.print("Enter lower price limit: ");
        double lprice=sc.nextDouble();
        System.out.print("Enter upper price limit: ");
        double hprice=sc.nextDouble();

        for (MenuObj c : list)
            if(c.getPrice()>lprice && c.getPrice()<hprice && discount==c.isDiscount()) {
                System.out.println(c);
            }
        Menu();
    }

    private static void AddFood(Scanner sc) {
        System.out.print("Enter food name: ");
        String name = sc.nextLine();
        System.out.print("Enter food price: ");
        String sPrice = sc.nextLine();
        double price= Double.parseDouble(sPrice);
        System.out.print("Enter food weight: ");
        String sWeight = sc.nextLine();
        double weight= Double.parseDouble(sWeight);
        System.out.print("Enter food discount(Y/N): ");
        String sDiscount = sc.nextLine();
        boolean discount = false;
        if(sDiscount=="Y"){
            discount=true;
        }
        if(sDiscount=="N"){
            discount=false;
        }

        em.getTransaction().begin();
        try {
            MenuObj c = new MenuObj(name, price, weight, discount);
            em.persist(c);
            em.getTransaction().commit();
            System.out.println(c.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
        Menu();
    }
}
