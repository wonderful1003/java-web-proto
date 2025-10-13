@RestController
@RequestMapping("/api/todos")
public class TodoController {
  private final TodoRepository repo;
  public TodoController(TodoRepository repo){ this.repo=repo; }

  @GetMapping public List<Todo> all(){ return repo.findAll(); }
  @PostMapping public Todo create(@RequestBody Todo t){ return repo.save(t); }
  @PatchMapping("/{id}")
  public Todo toggle(@PathVariable Long id){
    Todo t = repo.findById(id).orElseThrow();
    t.setCompleted(!t.isCompleted());
    return repo.save(t);
  }
  @DeleteMapping("/{id}") public void del(@PathVariable Long id){ repo.deleteById(id); }
}
