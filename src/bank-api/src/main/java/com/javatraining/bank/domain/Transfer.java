package com.javatraining.bank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a movement of funds between two accounts. Plain JPA entity — no business logic. Status
 * transitions are managed by the service layer.
 */
@Entity
@Table(name = "transfers")
public class Transfer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "from_account_id", nullable = false, updatable = false)
  private UUID fromAccountId;

  @Column(name = "to_account_id", nullable = false, updatable = false)
  private UUID toAccountId;

  @Column(name = "amount", nullable = false, precision = 18, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 50)
  private TransferStatus status;

  @Column(name = "failure_reason")
  private String failureReason;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  /** Required by JPA and for test construction. */
  public Transfer() {}

  @PrePersist
  void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }

  public UUID getId() {
    return id;
  }

  public UUID getFromAccountId() {
    return fromAccountId;
  }

  public void setFromAccountId(UUID fromAccountId) {
    this.fromAccountId = fromAccountId;
  }

  public UUID getToAccountId() {
    return toAccountId;
  }

  public void setToAccountId(UUID toAccountId) {
    this.toAccountId = toAccountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public TransferStatus getStatus() {
    return status;
  }

  public void setStatus(TransferStatus status) {
    this.status = status;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
