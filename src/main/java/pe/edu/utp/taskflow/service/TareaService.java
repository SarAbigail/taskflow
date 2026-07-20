package pe.edu.utp.taskflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.utp.taskflow.model.Estado;
import pe.edu.utp.taskflow.model.Prioridad;
import pe.edu.utp.taskflow.model.Tarea;
import pe.edu.utp.taskflow.repository.TareaRepository;

@Service
public class TareaService {

  private final TareaRepository tareaRepository;

  public TareaService(TareaRepository tareaRepository) {
    this.tareaRepository = tareaRepository;
  }

  public List<Tarea> listarTodas() {
    return tareaRepository.findAll();
  }

  public List<Tarea> buscar(
      String titulo,
      Estado estado,
      Prioridad prioridad) {

    String tituloNormalizado = normalizarTexto(titulo);

    return tareaRepository.buscarConFiltros(
        tituloNormalizado,
        estado,
        prioridad);
  }

  public Tarea guardar(Tarea tarea) {

    if (tarea.getId() != null) {
      Tarea tareaExistente = buscarPorId(tarea.getId());

      tarea.setFechaCreacion(
          tareaExistente.getFechaCreacion());
    }

    return tareaRepository.save(tarea);
  }

  public Tarea buscarPorId(Long id) {
    return tareaRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(
            "No se encontró la tarea con ID: " + id));
  }

  public void eliminar(Long id) {

    if (!tareaRepository.existsById(id)) {
      throw new IllegalArgumentException(
          "No se encontró la tarea con ID: " + id);
    }

    tareaRepository.deleteById(id);
  }

  public long contarTodas() {
    return tareaRepository.count();
  }

  public long contarPorEstado(Estado estado) {
    return tareaRepository.countByEstado(estado);
  }

  private String normalizarTexto(String texto) {

    if (texto == null || texto.isBlank()) {
      return "";
    }

    return texto.trim();
  }
}