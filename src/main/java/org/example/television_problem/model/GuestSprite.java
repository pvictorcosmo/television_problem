package org.example.television_problem.model;

public enum GuestSprite {
    NORMAL("/org/example/television_problem/view/assets/guest_sprite.png"),
    FRONT1("/org/example/television_problem/view/assets/sprites/front_1.png"),
    FRONT2("/org/example/television_problem/view/assets/sprites/front_2.png"),
    FRONT3("/org/example/television_problem/view/assets/sprites/front_3.png"),
    BACK1("/org/example/television_problem/view/assets/sprites/back_1.png"),
    BACK2("/org/example/television_problem/view/assets/sprites/back_2.png"),
    BACK3("/org/example/television_problem/view/assets/sprites/back_3.png"),
    LEFT1("/org/example/television_problem/view/assets/sprites/left_1.png"),
    LEFT2("/org/example/television_problem/view/assets/sprites/left_2.png"),
    RIGHT1("/org/example/television_problem/view/assets/sprites/right_1.png"),
    RIGHT2("/org/example/television_problem/view/assets/sprites/right_2.png");

    private final String path;

    // Construtor do Enum
    GuestSprite(String path) {
        this.path = path;
    }

    // Getter para o caminho do sprite
    public String getPath() {
        return path;
    }
}