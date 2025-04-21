package com.unicesumar.paymentMethods;

public enum PaymentType {
    PIX("PIX"),
    BOLETO("Boleto"),
    CARTAO("Cartão de Crédito");

    private final String descricao;

    PaymentType(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
