package pe.edu.utp.taskflow.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tareas")
public class Tarea {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El título es obligatorio")
  @Size(max = 100, message = "El título no puede superar los 100 caracteres")
  @Column(nullable = false, length = 100)
  private String titulo;

  @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
  @Column(length = 500)
  private String descripcion;

  @NotNull(message = "La prioridad es obligatoria")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Prioridad prioridad;

  @NotNull(message = "El estado es obligatorio")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Estado estado;

  @Column(nullable = false, updatable = false)
  private LocalDate fechaCreacion;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaVencimiento;

  @PrePersist
  public void establecerValoresIniciales() {
    if (fechaCreacion == null) {
      fechaCreacion = LocalDate.now();
    }

    if (estado == null) {
      estado = Estado.PENDIENTE;
    }
  }

  public Tarea() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Prioridad getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(Prioridad prioridad) {
    this.prioridad = prioridad;
  }

  public Estado getEstado() {
    return estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public LocalDate getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDate fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }
}