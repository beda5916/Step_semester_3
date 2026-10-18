/**
 * Payment Processing Shopping System
 * 
 * Features:
 * - Customer class with name
 * - Product class with name and price
 * - Order class with list of products (with quantities), status (PENDING, PAID)
 * - Abstract/Interface PaymentMethod with subclasses: CreditCardPayment, PayPalPayment, BankTransferPayment
 * - Each payment method processes payment differently (simulate success/failure)
 * - Order must have at least one item before payment
 * - Failed payment does NOT change order status to PAID
 * - Polymorphism for payment processing
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Q5_PaymentProcessingSystem {

    // ==========================================
    // Customer Class
    // ==========================================
    public static class Customer {
        private final String name;

        public Customer(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be null or empty.");
            }
            this.name = name.trim();
        }

        public String getName() {
            return name;
        }

        /**
         * Creates an order for this customer and announces creation.
         *
         * @return newly created Order
         */
        public Order createOrder() {
            return new Order(this, true);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // ==========================================
    // Product Class
    // ==========================================
    public static class Product {
        private final String name;
        private final double price;

        public Product(String name, double price) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be null or empty.");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Product price cannot be negative.");
            }
            this.name = name.trim();
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return name + " ($" + String.format("%.2f", price) + ")";
        }
    }

    // ==========================================
    // Order Item Class
    // ==========================================
    public static class OrderItem {
        private final Product product;
        private final int quantity;

        public OrderItem(Product product, int quantity) {
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getSubtotal() {
            return product.getPrice() * quantity;
        }

        public double getTotalPrice() {
            return getSubtotal();
        }

        @Override
        public String toString() {
            return product.getName() + " x " + quantity + " ($" + String.format("%.2f", getSubtotal()) + ")";
        }
    }

    // ==========================================
    // Order Status Enum
    // ==========================================
    public enum OrderStatus {
        PENDING("Pending"),
        PAID("Paid");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // ==========================================
    // PaymentMethod Abstract Base Class
    // ==========================================
    public static abstract class PaymentMethod {
        private final String methodName;

        public PaymentMethod(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        /**
         * Polymorphic payment processing method implemented differently by subclasses.
         *
         * @param amount the total amount to charge
         * @return true if payment succeeded, false otherwise
         */
        public abstract boolean processPayment(double amount);

        /**
         * Alias for processPayment to support polymorphic calls named pay().
         *
         * @param amount the total amount to charge
         * @return true if payment succeeded, false otherwise
         */
        public boolean pay(double amount) {
            return processPayment(amount);
        }
    }

    // ==========================================
    // CreditCardPayment Subclass
    // ==========================================
    public static class CreditCardPayment extends PaymentMethod {
        private final String cardNumber;
        private final String cardHolderName;
        private final String cvv;
        private final String expiryDate;
        private final boolean simulateSuccess;

        public CreditCardPayment(String cardNumber, String cardHolderName, String cvv, String expiryDate, boolean simulateSuccess) {
            super("Credit Card");
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
            this.cvv = cvv;
            this.expiryDate = expiryDate;
            this.simulateSuccess = simulateSuccess;
        }

        public CreditCardPayment(String cardNumber, String cvv, boolean simulateSuccess) {
            this(cardNumber, "", cvv, "", simulateSuccess);
        }

        public CreditCardPayment(String cardNumber, String cvv) {
            this(cardNumber, "", cvv, "", true);
        }

        public CreditCardPayment(boolean simulateSuccess) {
            this("1234-5678-9012-3456", "Cardholder", "123", "12/28", simulateSuccess);
        }

        public CreditCardPayment() {
            this(true);
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public String getCardHolderName() {
            return cardHolderName;
        }

        public String getCvv() {
            return cvv;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        @Override
        public boolean processPayment(double amount) {
            if (amount <= 0) {
                return false;
            }
            if (cardNumber == null || cardNumber.trim().isEmpty()) {
                return false;
            }
            // Simulates processing via card network
            return simulateSuccess;
        }
    }

    // ==========================================
    // PayPalPayment Subclass
    // ==========================================
    public static class PayPalPayment extends PaymentMethod {
        private final String email;
        private final boolean simulateSuccess;

        public PayPalPayment(String email, boolean simulateSuccess) {
            super("PayPal");
            this.email = email;
            this.simulateSuccess = simulateSuccess;
        }

        public PayPalPayment(String email) {
            this(email, true);
        }

        public PayPalPayment(boolean simulateSuccess) {
            this("user@paypal.com", simulateSuccess);
        }

        public PayPalPayment() {
            this(true);
        }

        public String getEmail() {
            return email;
        }

        @Override
        public boolean processPayment(double amount) {
            if (amount <= 0) {
                return false;
            }
            if (email == null || !email.contains("@")) {
                return false;
            }
            // Simulates PayPal API authentication and wallet balance check
            return simulateSuccess;
        }
    }

    // ==========================================
    // BankTransferPayment Subclass
    // ==========================================
    public static class BankTransferPayment extends PaymentMethod {
        private final String accountNumber;
        private final String bankName;
        private final String routingNumber;
        private final boolean simulateSuccess;

        public BankTransferPayment(String accountNumber, String bankName, String routingNumber, boolean simulateSuccess) {
            super("Bank Transfer");
            this.accountNumber = accountNumber;
            this.bankName = bankName;
            this.routingNumber = routingNumber;
            this.simulateSuccess = simulateSuccess;
        }

        public BankTransferPayment(String accountNumber, String bankName, boolean simulateSuccess) {
            this(accountNumber, bankName, "", simulateSuccess);
        }

        public BankTransferPayment(String accountNumber, String bankName) {
            this(accountNumber, bankName, "", true);
        }

        public BankTransferPayment(boolean simulateSuccess) {
            this("9876543210", "Standard Bank", "", simulateSuccess);
        }

        public BankTransferPayment() {
            this(true);
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getBankName() {
            return bankName;
        }

        public String getRoutingNumber() {
            return routingNumber;
        }

        @Override
        public boolean processPayment(double amount) {
            if (amount <= 0) {
                return false;
            }
            if (accountNumber == null || accountNumber.trim().isEmpty()) {
                return false;
            }
            // Simulates Automated Clearing House (ACH) / bank wire transfer
            return simulateSuccess;
        }
    }

    // ==========================================
    // Order Class
    // ==========================================
    public static class Order {
        private final Customer customer;
        private final List<OrderItem> items;
        private OrderStatus status;

        public Order(Customer customer) {
            this(customer, false);
        }

        public Order(Customer customer, boolean notifyCreation) {
            this.customer = customer;
            this.items = new ArrayList<>();
            this.status = OrderStatus.PENDING;
            if (notifyCreation && customer != null) {
                System.out.println("Order created for " + customer.getName() + ".");
            }
        }

        public Order() {
            this(null, false);
        }

        public Customer getCustomer() {
            return customer;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public List<OrderItem> getItems() {
            return Collections.unmodifiableList(items);
        }

        /**
         * Adds a product with specified quantity to the order.
         */
        public void addProduct(Product product, int quantity) {
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            items.add(new OrderItem(product, quantity));
        }

        /**
         * Adds a single product (quantity = 1) to the order.
         */
        public void addProduct(Product product) {
            addProduct(product, 1);
        }

        /**
         * Alias for addProduct.
         */
        public void addItem(Product product, int quantity) {
            addProduct(product, quantity);
        }

        /**
         * Alias for addProduct.
         */
        public void addItem(Product product) {
            addProduct(product, 1);
        }

        /**
         * Calculates total amount of all items in the order.
         */
        public double calculateTotal() {
            double total = 0.0;
            for (OrderItem item : items) {
                total += item.getSubtotal();
            }
            return total;
        }

        /**
         * Alias for calculateTotal.
         */
        public double getTotalAmount() {
            return calculateTotal();
        }

        /**
         * Processes payment using polymorphism.
         * Validates that the order has at least one item before attempting payment.
         * Failed payment does not change order status to PAID.
         *
         * @param paymentMethod the payment method strategy to use
         * @return true if payment succeeded and order is marked PAID; false otherwise
         */
        public boolean processPayment(PaymentMethod paymentMethod) {
            if (paymentMethod == null) {
                throw new IllegalArgumentException("Payment method cannot be null.");
            }

            // Order must have at least one item before payment
            if (items.isEmpty()) {
                System.out.println("Cannot process payment for an empty order.");
                return false;
            }

            String customerName = (customer != null) ? customer.getName() : "Customer";
            System.out.println("Payment initiated via " + paymentMethod.getMethodName() + " for Order of " + customerName + ".");

            double totalAmount = calculateTotal();
            boolean success = paymentMethod.processPayment(totalAmount);

            if (success) {
                this.status = OrderStatus.PAID;
                System.out.println("Payment for Order of " + customerName + " successful. Order status: " + status.getDisplayName() + ".");
                return true;
            } else {
                // Failed payment does NOT change order status to PAID (remains PENDING)
                System.out.println("Payment for Order of " + customerName + " failed. Order status: " + status.getDisplayName() + ".");
                return false;
            }
        }

        /**
         * Alias for processPayment.
         */
        public boolean pay(PaymentMethod paymentMethod) {
            return processPayment(paymentMethod);
        }
    }

    // ==========================================
    // Main Method Demonstrating Sample Flow
    // ==========================================
    public static void main(String[] args) {
        // Scenario 1: Customer X creates an order and pays successfully via Credit Card
        Customer customerX = new Customer("Customer X");
        Order orderX = customerX.createOrder();
        Product laptop = new Product("Laptop", 1200.0);
        orderX.addProduct(laptop, 1);
        PaymentMethod creditCard = new CreditCardPayment("4111-2222-3333-4444", "Customer X", "123", "12/28", true);
        orderX.processPayment(creditCard);

        // Scenario 2: Attempting to process payment for an empty order (must fail before initiating payment)
        Order emptyOrder = new Order(customerX);
        PaymentMethod bankTransfer = new BankTransferPayment("9876543210", "National Bank", true);
        emptyOrder.processPayment(bankTransfer);

        // Scenario 3: Customer Z creates an order, but payment fails via PayPal (order remains Pending)
        Customer customerZ = new Customer("Customer Z");
        Order orderZ = customerZ.createOrder();
        Product headphones = new Product("Headphones", 150.0);
        orderZ.addProduct(headphones, 2);
        PaymentMethod payPal = new PayPalPayment("customerZ@example.com", false); // simulate failure
        orderZ.processPayment(payPal);
    }
}
