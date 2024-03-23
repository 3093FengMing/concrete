package me.fengming.concrete.item.song;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.fengming.concrete.capability.ModCapabilities;
import me.fengming.concrete.entity.NPeltata;
import me.fengming.concrete.item.ConcreteTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class ConcreteGuanJu extends Item {
    private final Style SPECIAL_SKILL_NAME = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withUnderlined(true).withBold(true);
    private final Style SKILL_NAME = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withUnderlined(true);
    private final Style SKILL_DESC = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true);
    private final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("9C03D873-98C0-8F8F-53F0-05BF90B07DCC");
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ConcreteGuanJu() {
        super(new Item.Properties().rarity(Rarity.EPIC));
        float attackDamage = 4.0F + ConcreteTiers.CONCRETE_SONG.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Speed modifier", 0.06F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND || pEquipmentSlot == EquipmentSlot.OFFHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.desc.1").withStyle(SKILL_DESC));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.1.name").withStyle(SKILL_NAME));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.1.desc").withStyle(SKILL_DESC));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.2.name").withStyle(SKILL_NAME));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.2.desc").withStyle(SKILL_DESC));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.3.name").withStyle(SKILL_NAME));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.3.desc").withStyle(SKILL_DESC));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.4.name").withStyle(SKILL_NAME));
        pTooltipComponents.add(Component.translatable("tooltitp.concrete.guan_ju.skill.4.desc").withStyle(SKILL_DESC));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (pState.is(BlockTags.SWORD_EFFICIENT) && pLevel.random.nextDouble() <= 0.08) { // all plants
            pEntityLiving.heal((pEntityLiving.getMaxHealth() + pEntityLiving.getAbsorptionAmount()) * 0.05F);
            pLevel.playSound(null, pEntityLiving.getOnPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 0.6F, 0.2F);
            if (pLevel instanceof ServerLevel) {
                ((ServerLevel)pLevel).sendParticles(new DustParticleOptions(new Vector3f(0.506F, 0.675F, 0.506F), 1.2F),
                        pPos.getX(), pPos.getY() + 0.5D, pPos.getZ(),
                        4, 0.1D, 0.0D, 0.1D, 0.2D);
            }
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        boolean hasTag = pTarget.getTags().contains("guan_ju_targeted");
        if ((hasTag || pTarget.getRandom().nextDouble() <= 0.12) && !(pTarget instanceof NPeltata) && !pTarget.level().isClientSide) {

            int count = ModCapabilities.getCount(pTarget, "guan_ju");
            Player p = null;
            if (pTarget instanceof ServerPlayer) p = (ServerPlayer) pAttacker;

            ModCapabilities.setCount(p, pTarget, "guan_ju", ++count, -1);
            if (hasTag && count > 3) {
                pTarget.removeTag("guan_ju_targeted");
                ModCapabilities.clearCount(p, pTarget, "guan_ju");
                return doSpecialSkill(pTarget, pAttacker, count);
            }

            if (!hasTag) pTarget.addTag("guan_ju_targeted");
        }
        return true;
    }

    private boolean doSpecialSkill(LivingEntity target, LivingEntity attacker, int count) {
        Level world = target.level();
        if (!world.isClientSide) {
            target.hurt(target.damageSources().mobAttack(attacker), 3.0F * (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE));
            for (int i = 0; i < 2; i++) {
                double x = target.getRandomX(2.5F);
                double y = target.getY();
                double z = target.getRandomZ(2.5F);
                world.addFreshEntity(new NPeltata(world, x, y, z, attacker));
            }
        }
        return true;
    }
}
