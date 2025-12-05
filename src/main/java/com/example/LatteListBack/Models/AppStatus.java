package com.example.LatteListBack.Models;// 📁 package com.example.LatteListBack.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class AppStatus {

    // Usamos un ID fijo para asegurar que siempre trabajamos con la misma fila.
    @Id
    private String id = "SYNC_STATUS";

    // Campo que guardará la última vez que se hizo el GET a Geoapify.
    private LocalDateTime lastGeoapifySync;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getLastGeoapifySync() {
        return lastGeoapifySync;
    }

    public void setLastGeoapifySync(LocalDateTime lastGeoapifySync) {
        this.lastGeoapifySync = lastGeoapifySync;
    }
}