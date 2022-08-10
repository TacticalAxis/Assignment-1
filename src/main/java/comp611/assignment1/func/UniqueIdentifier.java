package comp611.assignment1.func;

import java.util.UUID;

public class UniqueIdentifier implements Comparable<UniqueIdentifier> {

    private final UUID uuid;

    public UniqueIdentifier() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(UniqueIdentifier o) {
        return this.uuid.compareTo(o.uuid);
    }
}
