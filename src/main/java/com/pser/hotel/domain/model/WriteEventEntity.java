package com.pser.hotel.domain.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.Transient;
import java.util.function.Consumer;

@MappedSuperclass
public class WriteEventEntity extends BaseEntity {
    @Transient
    private Consumer<WriteEventEntity> onCreatedEventHandler;

    @Transient
    private Consumer<WriteEventEntity> onUpdatedEventHandler;

    @Transient
    private Consumer<WriteEventEntity> onDeletedEventHandler;

    public void updateOnCreatedEventHandler(
            Consumer<WriteEventEntity> onCreatedEventHandler) {
        this.onCreatedEventHandler = onCreatedEventHandler;
    }

    public void updateOnUpdatedEventHandler(
            Consumer<WriteEventEntity> onUpdatedEventHandler) {
        this.onUpdatedEventHandler = onUpdatedEventHandler;
    }

    public void updateOnDeletedEventHandler(
            Consumer<WriteEventEntity> onDeletedEventHandler) {
        this.onDeletedEventHandler = onDeletedEventHandler;
    }

    @PostPersist
    private void onCreated() {
        if (onCreatedEventHandler == null) {
            return;
        }
        onCreatedEventHandler.accept(this);
    }

    @PostUpdate
    private void onUpdated() {
        if (onUpdatedEventHandler == null) {
            return;
        }
        onUpdatedEventHandler.accept(this);
    }

    @PostRemove
    private void onDeleted() {
        if (onDeletedEventHandler == null) {
            return;
        }
        onDeletedEventHandler.accept(this);
    }
}
