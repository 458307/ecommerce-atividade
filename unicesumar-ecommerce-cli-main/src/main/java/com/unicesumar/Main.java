package com.unicesumar;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.User;
import com.unicesumar.entities.Sale;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.UserRepository;
import com.unicesumar.repository.SaleRepository;
import com.unicesumar.paymentMethods.PaymentType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        ProductRepository listaDeProdutos = null;
        UserRepository listaDeUsuarios = null;
        SaleRepository listaDeVendas = null;

        Connection conn = null;
        String url = "jdbc:sqlite:database.sqlite";

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                listaDeProdutos = new ProductRepository(conn);
                listaDeUsuarios = new UserRepository(conn);
                listaDeVendas = new SaleRepository(conn);
            } else {
                System.out.println("Falha na conexão.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n---MENU---");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Cadastrar Usuário");
            System.out.println("4 - Listar Usuários");
            System.out.println("5 - Registrar Venda");
            System.out.println("6 - Listar Vendas");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Cadastrar Produto");
                    scanner.nextLine();
                    System.out.println("Digite o nome do produto:");
                    String nomeProduto = scanner.nextLine();
                    System.out.println("Digite o preço do produto:");
                    double precoProduto = scanner.nextDouble();
                    listaDeProdutos.save(new Product(nomeProduto, precoProduto));
                    System.out.println("Produto salvo com sucesso!");
                    break;

                case 2:
                    System.out.println("Listar Produtos");
                    List<Product> products = listaDeProdutos.findAll();
                    if (products.isEmpty()) {
                        System.out.println("Nenhum produto cadastrado.");
                    } else {
                        products.forEach(System.out::println);
                    }
                    break;

                case 3:
                    System.out.println("Cadastrar Usuário");
                    scanner.nextLine();
                    System.out.println("Digite o nome do usuário:");
                    String nomeUsuario = scanner.nextLine();
                    System.out.println("Digite o e-mail do usuário:");
                    String emailUsuario = scanner.nextLine();
                    if (!emailUsuario.contains("@")) {
                        System.out.println("E-mail inválido. Certifique-se de que contém '@'.");
                        break;
                    }
                    System.out.println("Digite a senha do usuário:");
                    String senhaUsuario = scanner.nextLine();
                    if (senhaUsuario.length() < 6) {
                        System.out.println("Senha inválida. A senha deve ter no mínimo 6 caracteres.");
                        break;
                    }
                    User novoUsuario = new User(nomeUsuario, emailUsuario, senhaUsuario);
                    listaDeUsuarios.save(novoUsuario);
                    System.out.println("Usuário cadastrado com sucesso!");
                    break;

                case 4:
                    System.out.println("Listar Usuários");
                    List<User> users = listaDeUsuarios.findAll();
                    if (users.isEmpty()) {
                        System.out.println("Nenhum usuário cadastrado.");
                    } else {
                        users.forEach(System.out::println);
                    }
                    break;
                    case 5:
                    System.out.println("Registrar Venda");
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println("Digite o Email do usuário:");
                    String email = scanner.nextLine();
                    Optional<User> cliente = listaDeUsuarios.findByEmail(email);

                    if (!cliente.isPresent()) {
                    System.out.println("Erro: Cliente com o email " + email + " não encontrado.");
                    break;
                    }

                    System.out.println("Usuário encontrado: " + cliente.get().getName());
                    System.out.println("Digite os IDs dos produtos (separados por vírgula):");
                    String idsProdutos = scanner.nextLine();
                    String[] ids = idsProdutos.split(",");
                    List<Product> produtosSelecionados = new LinkedList<>();
                    double totalPrice = 0.0;

                    for (String id : ids) {
                    try {
                    int productId = Integer.parseInt(id.trim());
                    Optional<Product> produto = listaDeProdutos.findById(productId);
                    if (produto.isPresent()) {
                    produtosSelecionados.add(produto.get());
                    totalPrice += produto.get().getPrice();
                    } else {
                    System.out.println("Erro: Produto com ID " + productId + " não encontrado.");
                    }
                    } catch (NumberFormatException e) {
                    System.out.println("Erro: ID inválido - " + id);
                    }
                    }

                    if (produtosSelecionados.isEmpty()) {
                    System.out.println("Nenhum produto válido foi selecionado.");
                    break;
                    }

                    System.out.println("\nEscolha a forma de pagamento:");
                    for (PaymentType type : PaymentType.values()) {
                    System.out.println((type.ordinal() + 1) + " - " + type.name());
                    }
                    System.out.print("Opção: ");
                    int opcaoPagamento = scanner.nextInt();
                    PaymentType paymentType;

                    if (opcaoPagamento > 0 && opcaoPagamento <= PaymentType.values().length) {
                    paymentType = PaymentType.values()[opcaoPagamento - 1];
                    } else {
                    System.out.println("Opção inválida. Venda não registrada.");
                    break;
                    }

                    System.out.println("\nAguarde, efetuando pagamento...");
                    String authKey = UUID.randomUUID().toString();
                    System.out.println("Pagamento confirmado com sucesso via " + paymentType.name() +". Chave de Autenticação: " + authKey);
                    int saleId = listaDeVendas.registerSale(cliente.get().getId(), totalPrice, paymentType.name());
                    for (Product produto : produtosSelecionados) {
                    listaDeVendas.registerSaleProduct(saleId, produto.getId(), 1);
                    }

                    System.out.println("\nResumo da venda:");
                    System.out.println("Cliente: " + cliente.get().getName());
                    System.out.println("Produtos:");
                    for (Product produto : produtosSelecionados) {
                        System.out.println("- " + produto.getName() + " (R$ " + String.format("%.2f", produto.getPrice()) + ")");
                    }
                    System.out.printf("Valor total: R$ %.2f\n", totalPrice);
                    System.out.println("Pagamento: " + paymentType.name());
                    System.out.println("\nVenda registrada com sucesso!");
                    break;

                case 6:
                    System.out.println("Listar Vendas");
                    List<Sale> sales = listaDeVendas.findAllSales();
                    if (sales.isEmpty()) {
                        System.out.println("Nenhuma venda registrada.");
                    } else {
                        for (Sale sale : sales) {
                            System.out.println("Venda ID: " + sale.getId());
                            System.out.println("Cliente: " + sale.getCustomerName());
                            System.out.printf("Total: R$ %.2f\n", sale.getTotalPrice());
                            System.out.println("Pagamento: " + sale.getPaymentType());

                            List<String> productList = listaDeVendas.findProductsBySaleId(sale.getId());
                            System.out.println("Produtos vendidos:");
                            if (productList.isEmpty()) {
                                System.out.println("- Nenhum produto associado a esta venda.");
                            } else {
                                for (String product : productList) {
                                    System.out.println("- " + product);
                                }
                            }
                            System.out.println("-----------------------------------");
                        }
                    }
                    break;

                case 7:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (option != 7);

        scanner.close();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
