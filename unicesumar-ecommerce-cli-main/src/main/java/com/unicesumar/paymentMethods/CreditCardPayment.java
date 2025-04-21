package com.unicesumar.paymentMethods;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagamento confirmado com sucesso via cartão de crédito");
    }
}
