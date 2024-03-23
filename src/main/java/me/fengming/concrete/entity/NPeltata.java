package me.fengming.concrete.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class NPeltata extends Entity implements TraceableEntity {

    private LivingEntity owner;
    private UUID ownerUUID;
    public NPeltata(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public NPeltata(Level pLevel, double pX, double pY, double pZ, LivingEntity pOwner) {
        this(EntityType.EVOKER_FANGS, pLevel);
        this.setOwner(pOwner);
        this.setPos(pX, pY, pZ);
    }

    public void setOwner(LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && !this.level().isClientSide) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) this.owner = (LivingEntity)entity;
        }
        return this.owner;
    }
}
