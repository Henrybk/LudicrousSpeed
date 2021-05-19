package ludicrousspeed.simulator.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ludicrousspeed.LudicrousSpeedMod;
import savestate.fastobjects.AnimationStateFast;

import java.util.Iterator;

public class IroncladPatches {
    @SpirePatch(
            clz = AbstractMonster.class,
            paramtypez = {DamageInfo.class},
            method = "damage"
    )
    public static class SpyOnMonsterDamageffectPatch {
        public static SpireReturn Prefix(AbstractMonster monster, DamageInfo info) {
            if (LudicrousSpeedMod.plaidMode) {
                if (info.output > 0 && monster.hasPower("IntangiblePlayer")) {
                    info.output = 1;
                }

                int damageAmount = info.output;
                if (!monster.isDying && !monster.isEscaping) {
                    if (damageAmount < 0) {
                        damageAmount = 0;
                    }

                    damageAmount = ReflectionHacks
                            .privateMethod(AbstractCreature.class, "decrementBlock", DamageInfo.class, int.class)
                            .invoke(monster, info, damageAmount);

                    Iterator var5;
                    AbstractRelic r;
                    if (info.owner == AbstractDungeon.player) {
                        for (var5 = AbstractDungeon.player.relics.iterator(); var5
                                .hasNext(); damageAmount = r
                                .onAttackToChangeDamage(info, damageAmount)) {
                            r = (AbstractRelic) var5.next();
                        }
                    }

                    AbstractPower p;
                    if (info.owner != null) {
                        for (var5 = info.owner.powers.iterator(); var5.hasNext(); damageAmount = p
                                .onAttackToChangeDamage(info, damageAmount)) {
                            p = (AbstractPower) var5.next();
                        }
                    }

                    for (var5 = monster.powers.iterator(); var5.hasNext(); damageAmount = p
                            .onAttackedToChangeDamage(info, damageAmount)) {
                        p = (AbstractPower) var5.next();
                    }

                    if (info.owner == AbstractDungeon.player) {
                        var5 = AbstractDungeon.player.relics.iterator();

                        while (var5.hasNext()) {
                            r = (AbstractRelic) var5.next();
                            r.onAttack(info, damageAmount, monster);
                        }
                    }

                    var5 = monster.powers.iterator();

                    while (var5.hasNext()) {
                        p = (AbstractPower) var5.next();
                        p.wasHPLost(info, damageAmount);
                    }

                    if (info.owner != null) {
                        var5 = info.owner.powers.iterator();

                        while (var5.hasNext()) {
                            p = (AbstractPower) var5.next();
                            p.onAttack(info, damageAmount, monster);
                        }
                    }


                    for (var5 = monster.powers.iterator(); var5.hasNext(); damageAmount = p
                            .onAttacked(info, damageAmount)) {
                        p = (AbstractPower) var5.next();
                    }


                    monster.lastDamageTaken = Math.min(damageAmount, monster.currentHealth);
                    if (damageAmount > 0) {
                        if (damageAmount >= 99 && !CardCrawlGame.overkill) {
                            CardCrawlGame.overkill = true;
                        }

                        monster.currentHealth -= damageAmount;

                        if (monster.currentHealth < 0) {
                            monster.currentHealth = 0;
                        }

                    }

                    if (monster.currentHealth <= 0) {

                        long deathStartTime = System.currentTimeMillis();
                        monster.die();

                        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                            AbstractDungeon.actionManager.cleanCardQueue();
                        }

                        if (monster.currentBlock > 0) {
                            monster.loseBlock();
                        }
                    }

                }

                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = Ironclad.class,
            paramtypez = {String.class},
            method = SpirePatch.CONSTRUCTOR
    )
    public static class NoAnimationsPatch {
        @SpireInsertPatch(loc = 79)
        public static SpireReturn Sentry(Ironclad _instance, String playerName) {
            if (LudicrousSpeedMod.plaidMode) {
                _instance.state = new AnimationStateFast();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            paramtypez = {SpriteBatch.class},
            method = "render"
    )
    public static class DisableRenderIroncladPatch {
        public static SpireReturn Prefix(AbstractPlayer _instance, SpriteBatch sprites) {
            if (LudicrousSpeedMod.plaidMode) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
