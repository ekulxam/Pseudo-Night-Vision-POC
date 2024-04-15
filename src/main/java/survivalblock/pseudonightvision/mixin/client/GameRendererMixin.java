package survivalblock.pseudonightvision.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // for this example I used StatusEffects.POISON, which you should replace with your own
    // YOU SHOULD ONLY CHOOSE ONE OF THE TWO HANDLER METHODS BELOW, NOT BOTH



    @Inject(method = "getNightVisionStrength", at = @At(value = "HEAD"), cancellable = true)
    private static void returnThat(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir){
        if (entity.hasStatusEffect(StatusEffects.POISON)) {
            if (entity.hasStatusEffect(StatusEffects.NIGHT_VISION) && entity.getStatusEffect(StatusEffects.NIGHT_VISION).getDuration() > entity.getStatusEffect(StatusEffects.POISON).getDuration()) {
                return;
            }
            // Below code simulates night vision in vanilla. I do not recommend going this route as another mod
            // which is mixin-ing into this method won't be able to modify your code if they remove night vision
            // flashing or something simular
            // However this does work with iris
            StatusEffectInstance statusEffectInstance = entity.getStatusEffect(StatusEffects.POISON);
            cir.setReturnValue(!statusEffectInstance.isDurationBelow(200) ? 1.0F : 0.7F + MathHelper.sin(((float)statusEffectInstance.getDuration() - tickDelta) * 3.1415927F * 0.2F) * 0.3F);
        }
    }


    @ModifyExpressionValue(method = "getNightVisionStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
    private static StatusEffectInstance substitutePseudoNightVision(StatusEffectInstance original, LivingEntity entity, float tickDelta){
        // I personally think that the ModifyExpressionValue mixin is better because it allows
        // other mods to modify your logic (such as a mod which might want to patch the night
        // vision flashing), but both should work the same.
        // This does not work with iris
        return original == null ? entity.getStatusEffect(StatusEffects.POISON) : original;
    }
}