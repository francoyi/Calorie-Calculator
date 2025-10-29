
package com.caloriecalc.repo;
import com.caloriecalc.util.JsonUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
abstract class AbstractJsonRepository<T> {
  protected final Path file;
  protected AbstractJsonRepository(Path file){ this.file = file; }
  protected synchronized T loadOrDefault(T def, java.lang.reflect.Type type){
    try {
      if (!Files.exists(file)) return def;
      byte[] bytes = Files.readAllBytes(file);
      if (bytes.length==0) return def;
      return JsonUtil.mapper().readValue(bytes, JsonUtil.mapper().constructType(type));
    } catch (IOException e){ throw new RuntimeException("Failed to load json: "+e.getMessage(), e); }
  }
  protected synchronized void atomicWrite(Object root){
    try {
      Files.createDirectories(file.getParent());
      Path tmp = file.resolveSibling(file.getFileName()+".tmp");
      Files.writeString(tmp, JsonUtil.toJson(root));
      Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e){ throw new RuntimeException("Failed to write json: "+e.getMessage(), e); }
  }
}
