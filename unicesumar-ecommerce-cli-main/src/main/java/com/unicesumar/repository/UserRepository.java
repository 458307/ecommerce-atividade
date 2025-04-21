package com.unicesumar.repository;

import com.unicesumar.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements EntityRepository<User> {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User entity) {
        String query = "INSERT INTO User (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getEmail());
            stmt.setString(3, entity.getPassword());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(int id) {
        String query = "SELECT * FROM User WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM User";
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        }

        return users;
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM User WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void registerSale(int userId, double totalPrice, String paymentType) {
        String sql = "INSERT INTO Sale (userId, totalPrice, paymentType) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, totalPrice);
            stmt.setString(3, paymentType);
            stmt.executeUpdate();
            System.out.println("Venda registrada com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar venda: " + e.getMessage());
        }
    }
}