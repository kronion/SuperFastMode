package skrelpoid.superfastmode.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class InstantAttacksPatch {
    private static final Logger logger = LogManager.getLogger(InstantAttacksPatch.class.getName());

    @SpirePatch(
            clz = AnimateSlowAttackAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class AnimationPatch {
        public static void Prefix(AnimateSlowAttackAction __instance) {
            __instance.isDone = true;
        }
    }

    @SpirePatch(
            clz = AnimateFastAttackAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class FastAnimationPatch {
        public static void Prefix(AnimateFastAttackAction __instance) {
            __instance.isDone = true;
        }
    }

    @SpirePatch(
            clz = EndTurnAction.class,
            method = "update"
    )
    public static class NoEnemyTurnEffect {
        public static void Replace(EndTurnAction _instance) {
            AbstractDungeon.actionManager.endTurn();
            _instance.isDone = true;
        }
    }

    @SpirePatch(
            clz = MonsterStartTurnAction.class,
            method = "update"
    )
    public static class InstantMonsterStartTurnActions {
        public static void Replace(MonsterStartTurnAction __instance) {
            __instance.isDone = true;
            (AbstractDungeon.getCurrRoom()).monsters.applyPreTurnLogic();
        }
    }

    @SpirePatch(
        clz = WaitAction.class,
        method = "update"
    )
    public static class InstantWaitAction {
        public static void Replace(WaitAction __instance) {
            __instance.isDone = true;
        }
    }

    @SpirePatch(
        clz = DiscardAtEndOfTurnAction.class,
        method = "update"
    )
    public static class InstantDiscardAtEndOfTurnAction {
        public static void Prefix(DiscardAtEndOfTurnAction __instance) {
            float DURATION = ReflectionHacks.getPrivateStatic(DiscardAtEndOfTurnAction.class, "DURATION");
            ReflectionHacks.setPrivateInherited(__instance, DiscardAtEndOfTurnAction.class, "duration", DURATION);
        }
    }

    @SpirePatch(
        clz = DiscardAction.class,
        method = "update"
    )
    public static class InstantDiscardAction {
        public static void Postfix(DiscardAction __instance) {
            boolean endTurn = ReflectionHacks.getPrivate(__instance, DiscardAction.class, "endTurn");
                if (endTurn) {
                    __instance.isDone = true;
                }
        }
    }

    @SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = {
            boolean.class
        }
    )
    public static class InstantMonsterDeaths {
        public static void Postfix(AbstractMonster __instance) {
            __instance.deathTimer = 0.0f;
        }
    }

    @SpirePatch(
        clz = CardCrawlGame.class,
        method = "fadeToBlack"
    )
    public static class InstantFadeOut {
        public static void Postfix() {
            // Must not be exactly 0
            CardCrawlGame.screenTimer = 0.01f;
        }
    }

    @SpirePatch(
        clz = CardCrawlGame.class,
        method = "fadeIn"
    )
    public static class InstantFadeIn {
        public static void Postfix() {
            // Must not be exactly 0
            CardCrawlGame.screenTimer = 0.01f;
        }
    }

	@SpirePatch(
        clz = AbstractMonster.class,
        method = "escape"
    )
    public static class InstantMonsterEscape {
        public static void Postfix(AbstractMonster __instance) {
			// Must not be exactly 0
            __instance.escapeTimer = 0.01f;
        }
    }

	@SpirePatch(
        clz = SmokeBomb.class,
        method = "use"
    )
    public static class InstantSmokeBombEscape {
        public static void Postfix() {
			// Must not be exactly 0
			AbstractDungeon.player.escapeTimer = 0.01f;
        }
    }
}