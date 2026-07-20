package pe.edu.utp.taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.utp.taskflow.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
}