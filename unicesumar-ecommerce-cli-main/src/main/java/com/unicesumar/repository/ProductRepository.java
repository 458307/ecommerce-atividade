package com.unicesumar.repository;

import com.unicesumar.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.LinkedList;

public class ProductRepository implements EntityRepository<Product> {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product entity) {
        String query = "INSERT INTO Product (name, price) VALUES (?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setDouble(2, entity.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    @Override
    public Optional<Product> findById(int id) {
        String query = "SELECT * FROM Product WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Product product = new Product(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT * FROM Product";
        List<Product> products = new LinkedList<>();
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
        return products;
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM Product WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Produto deletado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto: " + e.getMessage());
        }
    }
}
