package me.fengming.concrete.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class NPeltata extends PathfinderMob implements OwnableEntity {
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(NPeltata.class, EntityDataSerializers.OPTIONAL_UUID);

    public NPeltata(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setOwner(null);
    }

    public NPeltata(Level pLevel, double pX, double pY, double pZ, LivingEntity pOwner) {
        this(EntityRegistry.N_PELTATA.get(), pLevel);
        this.setOwner(pOwner.getUUID());
        this.setPos(pX, pY, pZ);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, -0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NPeltataOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NPeltataOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, 5, true, true, null));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean bl = super.hurt(pSource, pAmount);
        if (this.getHealth() <= 0.0f) {
            this.discard();
            return false;
        }
        return bl;
    }

    protected static class NPeltataOwnerHurtByTargetGoal extends TargetGoal {
        private final NPeltata mob;
        private LivingEntity ownerLastHurtBy;
        private int timestamp;

        public NPeltataOwnerHurtByTargetGoal(NPeltata mob) {
            super(mob, false);
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        public boolean canUse() {
            LivingEntity owner = this.mob.getOwner();
            if (owner == null) return false;
            this.ownerLastHurtBy = owner.getLastHurtByMob();
            int lastAttack = owner.getLastHurtByMobTimestamp();
            return lastAttack != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && !(this.ownerLastHurtBy instanceof NPeltata);
        }

        public void start() {
            this.mob.setTarget(this.ownerLastHurtBy);
            LivingEntity owner = this.mob.getOwner();
            if (owner != null) this.timestamp = owner.getLastHurtByMobTimestamp();
            super.start();
        }
    }

    protected static class NPeltataOwnerHurtTargetGoal extends TargetGoal {
        private final NPeltata mob;
        private LivingEntity ownerLastHurt;
        private int timestamp;

        public NPeltataOwnerHurtTargetGoal(NPeltata mob) {
            super(mob, false);
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        public boolean canUse() {
            LivingEntity owner = this.mob.getOwner();
            if (owner == null) return false;
            this.ownerLastHurt = owner.getLastHurtMob();
            int lastAttack = owner.getLastHurtMobTimestamp();
            return lastAttack != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && !(this.ownerLastHurt instanceof NPeltata);

        }

        public void start() {
            this.mob.setTarget(this.ownerLastHurt);
            LivingEntity owner = this.mob.getOwner();
            if (owner != null) this.timestamp = owner.getLastHurtMobTimestamp();
            super.start();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.entityData.set(DATA_OWNER_UUID, Optional.of(pCompound.getUUID("Owner")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WET_GRASS_BREAK;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AZALEA_BREAK;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return pEntity instanceof NPeltata || pEntity.isPushable() || pEntity.canBeCollidedWith();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity pEntity) {
        super.push(pEntity);
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    public void setOwner(UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

}
