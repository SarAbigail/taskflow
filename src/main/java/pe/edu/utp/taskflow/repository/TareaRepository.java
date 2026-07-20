package pe.edu.utp.taskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.utp.taskflow.model.Estado;
import pe.edu.utp.taskflow.model.Prioridad;
import pe.edu.utp.taskflow.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

  long countByEstado(Estado estado);

  @Query("""
      SELECT t
      FROM Tarea t
      WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
        AND (:estado IS NULL OR t.estado = :estado)
        AND (:prioridad IS NULL OR t.prioridad = :prioridad)
      ORDER BY t.fechaCreacion DESC, t.id DESC
      """)
  List<Tarea> buscarConFiltros(
      @Param("titulo") String titulo,
      @Param("estado") Estado estado,
      @Param("prioridad") Prioridad prioridad);
}