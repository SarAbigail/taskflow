package pe.edu.utp.taskflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.taskflow.model.Estado;
import pe.edu.utp.taskflow.model.Prioridad;
import pe.edu.utp.taskflow.model.Tarea;
import pe.edu.utp.taskflow.service.TareaService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TareaController {

  private final TareaService tareaService;

  public TareaController(TareaService tareaService) {
    this.tareaService = tareaService;
  }

  @GetMapping("/")
  public String mostrarInicio(
      @RequestParam(required = false) String titulo,
      @RequestParam(required = false) Estado estado,
      @RequestParam(required = false) Prioridad prioridad,
      Model model) {

    model.addAttribute(
        "tareas",
        tareaService.buscar(titulo, estado, prioridad));

    model.addAttribute(
        "totalTareas",
        tareaService.contarTodas());

    model.addAttribute(
        "tareasPendientes",
        tareaService.contarPorEstado(Estado.PENDIENTE));

    model.addAttribute(
        "tareasEnProgreso",
        tareaService.contarPorEstado(Estado.EN_PROGRESO));

    model.addAttribute(
        "tareasCompletadas",
        tareaService.contarPorEstado(Estado.COMPLETADA));

    model.addAttribute("tituloFiltro", titulo);
    model.addAttribute("estadoFiltro", estado);
    model.addAttribute("prioridadFiltro", prioridad);

    model.addAttribute("estados", Estado.values());
    model.addAttribute("prioridades", Prioridad.values());

    return "index";
  }

  @GetMapping("/tareas/nueva")
  public String mostrarFormularioNuevaTarea(Model model) {
    Tarea tarea = new Tarea();
    tarea.setEstado(Estado.PENDIENTE);
    tarea.setPrioridad(Prioridad.MEDIA);

    model.addAttribute("tarea", tarea);
    model.addAttribute("estados", Estado.values());
    model.addAttribute("prioridades", Prioridad.values());

    return "tareas/form";
  }

  @PostMapping("/tareas/guardar")
  public String guardarTarea(
      @Valid @ModelAttribute("tarea") Tarea tarea,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("estados", Estado.values());
      model.addAttribute("prioridades", Prioridad.values());

      return "tareas/form";
    }

    boolean esNueva = tarea.getId() == null;

    tareaService.guardar(tarea);

    if (esNueva) {
      redirectAttributes.addFlashAttribute(
          "mensajeExito",
          "La tarea fue registrada correctamente.");
    } else {
      redirectAttributes.addFlashAttribute(
          "mensajeExito",
          "La tarea fue actualizada correctamente.");
    }

    return "redirect:/";
  }

  @GetMapping("/tareas/editar/{id}")
  public String mostrarFormularioEditar(
      @PathVariable Long id,
      Model model) {

    model.addAttribute("tarea", tareaService.buscarPorId(id));
    model.addAttribute("estados", Estado.values());
    model.addAttribute("prioridades", Prioridad.values());

    return "tareas/form";
  }

  @PostMapping("/tareas/eliminar/{id}")
  public String eliminarTarea(
      @PathVariable Long id,
      RedirectAttributes redirectAttributes) {

    tareaService.eliminar(id);

    redirectAttributes.addFlashAttribute(
        "mensajeExito",
        "La tarea fue eliminada correctamente.");

    return "redirect:/";
  }
}