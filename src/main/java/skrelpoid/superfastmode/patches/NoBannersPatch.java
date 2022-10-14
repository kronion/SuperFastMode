package skrelpoid.superfastmode.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.vfx.EnemyTurnEffect;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;


public class NoBannersPatch {
	@SpirePatch(
		clz = EnemyTurnEffect.class,
		method = "update"
	)
	public static class NoEnemyTurnBanner {
		public static void Postfix(EnemyTurnEffect __instance) {
			__instance.isDone = true;
		}
	}

	@SpirePatch(
		clz = PlayerTurnEffect.class,
		method = "update"
	)
	public static class NoPlayerTurnBanner {
		public static void Prefix(PlayerTurnEffect __instance) {
			__instance.duration = 0.0f;
		}
	}

	@SpirePatch(
		clz = BattleStartEffect.class,
		method = "update"
	)
	public static class NoBattleStartBanner {
		public static void Prefix(BattleStartEffect __instance) {
			__instance.duration = 0.0f;
			ReflectionHacks.setPrivate(__instance, BattleStartEffect.class, "timer1", 0.0f);

		}
	}
}
