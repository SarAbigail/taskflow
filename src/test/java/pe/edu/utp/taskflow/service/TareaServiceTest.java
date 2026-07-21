package pe.edu.utp.taskflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.utp.taskflow.model.Estado;
import pe.edu.utp.taskflow.model.Prioridad;
import pe.edu.utp.taskflow.model.Tarea;
import pe.edu.utp.taskflow.repository.TareaRepository;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

  @Mock
  private TareaRepository tareaRepository;

  private TareaService tareaService;

  private Tarea tarea;

  @BeforeEach
  void configurar() {
    tareaService = new TareaService(tareaRepository);

    tarea = new Tarea();
    tarea.setId(1L);
    tarea.setTitulo("Preparar informe final");
    tarea.setDescripcion("Completar el informe del proyecto");
    tarea.setPrioridad(Prioridad.ALTA);
    tarea.setEstado(Estado.PENDIENTE);
    tarea.setFechaVencimiento(LocalDate.now().plusDays(5));
    tarea.setFechaCreacion(LocalDate.now());
  }

  @Test
  void debeListarTodasLasTareas() {
    when(tareaRepository.findAll())
        .thenReturn(List.of(tarea));

    List<Tarea> resultado = tareaService.listarTodas();

    assertThat(resultado).hasSize(1);
    assertThat(resultado.getFirst().getTitulo())
        .isEqualTo("Preparar informe final");

    verify(tareaRepository).findAll();
  }

  @Test
  void debeBuscarTareaPorId() {
    when(tareaRepository.findById(1L))
        .thenReturn(Optional.of(tarea));

    Tarea resultado = tareaService.buscarPorId(1L);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getId()).isEqualTo(1L);
    assertThat(resultado.getPrioridad())
        .isEqualTo(Prioridad.ALTA);

    verify(tareaRepository).findById(1L);
  }

  @Test
  void debeLanzarExcepcionCuandoLaTareaNoExiste() {
    when(tareaRepository.findById(99L))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> tareaService.buscarPorId(99L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("99");

    verify(tareaRepository).findById(99L);
  }

  @Test
  void debeGuardarUnaTareaNueva() {
    Tarea nuevaTarea = new Tarea();
    nuevaTarea.setTitulo("Configurar GitHub Actions");
    nuevaTarea.setDescripcion("Crear el pipeline del proyecto");
    nuevaTarea.setPrioridad(Prioridad.MEDIA);
    nuevaTarea.setEstado(Estado.PENDIENTE);

    when(tareaRepository.save(nuevaTarea))
        .thenReturn(nuevaTarea);

    Tarea resultado = tareaService.guardar(nuevaTarea);

    assertThat(resultado.getTitulo())
        .isEqualTo("Configurar GitHub Actions");

    verify(tareaRepository).save(nuevaTarea);
    verify(tareaRepository, never()).findById(1L);
  }

  @Test
  void debeConservarLaFechaDeCreacionAlEditar() {
    LocalDate fechaOriginal = LocalDate.of(2026, 7, 19);

    tarea.setFechaCreacion(fechaOriginal);

    Tarea tareaEditada = new Tarea();
    tareaEditada.setId(1L);
    tareaEditada.setTitulo("Informe actualizado");
    tareaEditada.setDescripcion("Nueva descripción");
    tareaEditada.setPrioridad(Prioridad.MEDIA);
    tareaEditada.setEstado(Estado.EN_PROGRESO);

    when(tareaRepository.findById(1L))
        .thenReturn(Optional.of(tarea));

    when(tareaRepository.save(tareaEditada))
        .thenReturn(tareaEditada);

    Tarea resultado = tareaService.guardar(tareaEditada);

    assertThat(resultado.getFechaCreacion())
        .isEqualTo(fechaOriginal);

    verify(tareaRepository).findById(1L);
    verify(tareaRepository).save(tareaEditada);
  }

  @Test
  void debeEliminarUnaTareaExistente() {
    when(tareaRepository.existsById(1L))
        .thenReturn(true);

    tareaService.eliminar(1L);

    verify(tareaRepository).existsById(1L);
    verify(tareaRepository).deleteById(1L);
  }

  @Test
  void noDebeEliminarUnaTareaInexistente() {
    when(tareaRepository.existsById(99L))
        .thenReturn(false);

    assertThatThrownBy(() -> tareaService.eliminar(99L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("99");

    verify(tareaRepository).existsById(99L);
    verify(tareaRepository, never()).deleteById(99L);
  }

  @Test
  void debeContarTareasPorEstado() {
    when(tareaRepository.countByEstado(Estado.PENDIENTE))
        .thenReturn(3L);

    long resultado = tareaService.contarPorEstado(Estado.PENDIENTE);

    assertThat(resultado).isEqualTo(3L);

    verify(tareaRepository)
        .countByEstado(Estado.PENDIENTE);
  }

  @Test
  void debeBuscarAplicandoFiltros() {
    when(tareaRepository.buscarConFiltros(
        "informe",
        Estado.PENDIENTE,
        Prioridad.ALTA)).thenReturn(List.of(tarea));

    List<Tarea> resultado = tareaService.buscar(
        " informe ",
        Estado.PENDIENTE,
        Prioridad.ALTA);

    assertThat(resultado).hasSize(1);

    verify(tareaRepository).buscarConFiltros(
        "informe",
        Estado.PENDIENTE,
        Prioridad.ALTA);
  }

  @Test
  void debeConvertirTituloVacioEnCadenaVacia() {
    when(tareaRepository.buscarConFiltros(
        "",
        null,
        null)).thenReturn(List.of(tarea));

    List<Tarea> resultado = tareaService.buscar(null, null, null);

    assertThat(resultado).hasSize(1);

    verify(tareaRepository).buscarConFiltros(
        "",
        null,
        null);
  }
}