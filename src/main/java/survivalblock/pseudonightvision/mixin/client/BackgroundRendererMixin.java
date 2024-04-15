package survivalblock.pseudonightvision.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Debug(export = true)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

	@ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", ordinal = 0))
	private static boolean accountForPseudoNightVision(boolean original, @Local(ordinal = 0) LivingEntity livingEntity2){
		return original || livingEntity2.hasStatusEffect(StatusEffects.POISON);
	}
}