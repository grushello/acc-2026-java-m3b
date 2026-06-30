package org.example.menu;

import org.example.config.AppConfig;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.PaymentResult;
import org.example.payment.PaymentMethod;
import org.example.payment.PaymentMethodFactory;
import org.example.payment.PaymentProcessor;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();

    private ArrayList<Order> completedOrders = new ArrayList<Order>();
    private ArrayList<Order> activeOrders = new ArrayList<Order>();
    private Order currentOrder;
    public void start(){
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to " + config.getApplicationName());
        System.out.println("Currency: " + config.getCurrency());
        System.out.println("Tax rate: " + config.getTaxRate() + "\n");
        boolean running = true;
        while(running){
            printMenu();

            int option = Integer.parseInt(scanner.nextLine());

            try {
                switch (option) {
                    case 1 -> createOrder();
                    case 2 -> addItem();
                    case 3 -> viewOrder();
                    case 4 -> payOrder();
                    case 5 -> viewCompletedOrders();
                    case 6 -> viewActiveOrders();
                    case 7 -> switchOrder();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createOrder(){
        System.out.println("Customer name:");
        String customerName = scanner.nextLine();

        currentOrder = Order.builder()
                .customerName(customerName)
                .build();
        activeOrders.add(currentOrder);
        System.out.println("Order created for " + customerName);
    }

    private void addItem(){
        if(currentOrder == null){
            throw new IllegalStateException("Cannot add items to non-existing order");
        }

        System.out.println("Item name:");
        String itemName = scanner.nextLine();

        System.out.println("Price:");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.println("Quantity:");
        int quantity = Integer.parseInt(scanner.nextLine());

        currentOrder.addItem(new OrderItem(itemName, price, quantity));
        System.out.println("Item added to order");
    }

    private void viewOrder(){
        if(currentOrder == null){
            throw new IllegalStateException("Order to be viewed does not exist");
        }

        System.out.println("Customer: " + currentOrder.getCustomerName());
        System.out.println("Status: " +  currentOrder.getStatus());
        System.out.println("Items:");

        for (OrderItem item : currentOrder.getItems()){
            System.out.println("- " + item);
        }

        System.out.println("Total: " + currentOrder.calculateTotal() + " (including taxes and discounts)");
    }

    private void payOrder(){
        if(currentOrder == null){
            throw new IllegalStateException("Order to be paid does not exist");
        }

        System.out.println("""
                Select payment method:
                1. Credit Card
                2. PayPal
                3. Gift Card
                4. Bitcoin
                """);
        int option = Integer.parseInt(scanner.nextLine());

        PaymentMethod paymentMethod = switch(option){
            case 1 -> createCreditCardPayment();
            case 2 -> createPaypalPayment();
            case 3 -> createGiftCardPayment();
            case 4 -> createBitcoinPayment();
            default -> throw new IllegalArgumentException("Invalid payment method");
        };

        PaymentResult result = paymentProcessor.process(currentOrder, paymentMethod);
        System.out.println(result.getMessage());
        if(result.isSuccessful()){
            completedOrders.add(currentOrder);
            activeOrders.remove(currentOrder);
            if(!activeOrders.isEmpty())
                currentOrder = activeOrders.get(0);
            else
                currentOrder = null;
        }
    }

    private void viewCompletedOrders(){
        System.out.println("Completed orders:\n");
        for(int i = 0; i < completedOrders.size(); i++)
        {
            System.out.printf("Completed Order %d:\n\n", i + 1);
            System.out.print(completedOrders.get(i).toString() + "\n\n");
        }
    }
    private void viewActiveOrders(){
        System.out.println("Active orders:\n");
        for(int i = 0; i < activeOrders.size(); i++)
        {
            System.out.printf("Active order %d:\n", i + 1);
            System.out.print(activeOrders.get(i).toString() + "\n\n");
        }
    }
    private void switchOrder(){
        System.out.println("Active order number (1-based):\n");
        int i = scanner.nextInt() - 1;
        scanner.nextLine();
        if(i >= activeOrders.size() || i < 0)
            throw new IndexOutOfBoundsException("Incorrect order index");
        currentOrder = activeOrders.get(i);
    }
    private PaymentMethod createCreditCardPayment(){
        System.out.println("Card number:");
        String cardNumber =  scanner.nextLine();

        System.out.println("Card holder name:");
        String cardHolderName =  scanner.nextLine();

        return PaymentMethodFactory.createCreditCardPayment(cardNumber,cardHolderName);
    }

    private PaymentMethod createPaypalPayment(){
        System.out.println("Email:");
        String email =  scanner.nextLine();

        return PaymentMethodFactory.createPayPalPayment(email);
    }

    private PaymentMethod createGiftCardPayment(){
        System.out.println("Code:");
        String code =  scanner.nextLine();

        System.out.println("Balance:");
        double balance =  scanner.nextDouble();
        scanner.nextLine();

        return PaymentMethodFactory.createGiftCardPayment(code, balance);
    }
    private PaymentMethod createBitcoinPayment(){
        System.out.println("Wallet address:");
        String walletAddress =  scanner.nextLine();

        return PaymentMethodFactory.createBitcoinPayment(walletAddress);
    }

    private void printMenu(){
        System.out.println("""
                1. Create order
                2. Add item to order
                3. View order
                4. Pay order
                5. View completed orders
                6. View active orders
                7. Switch to other order
                0. Exit
                """);
    }
}
