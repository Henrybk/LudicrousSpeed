package ludicrousspeed.simulator.commands;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Transmutation;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.Wound;

import java.util.HashMap;

public class ExhaustOrder {
    public static HashMap<String, Integer> uglyThing;
    public static final HashMap<String, Integer> CARD_RANKS = makeRank();

    public static HashMap<String, Integer> makeRank() {
        uglyThing = new HashMap<>();

        add(new Sentinel());

        // non-exhausting statuses and curses
        add(new Normality());
        add(new Burn());
        add(new Pain());
        add(new Regret());
        add(new Shame());
        add(new Writhe());
        add(new Doubt());

        add(new Wound());
        add(new Slimed());
        add(new CurseOfTheBell());
        add(new Injury());
        add(new Necronomicurse());

        // Strikes and Defends
        add(new Defend_Red());
        add(new Strike_Red());

        add(new Madness());
        add(new Shockwave());
        add(new SpotWeakness());
        add(new Transmutation());
        add(new Whirlwind());
        add(new Rage());
        add(new Offering());
        add(new Intimidate());
        add(new Flex());
        add(new DoubleTap());
        add(new DualWield());
        add(new Clash());

        add(new Bloodletting());
        add(new Berserk());
        add(new BattleTrance());

        add(new Armaments());
        add(new Inflame());
        add(new Rupture());
        add(new Juggernaut());
        add(new FireBreathing());
        add(new FeelNoPain());
        add(new Evolve());
        add(new DemonForm());
        add(new Barricade());
        add(new Brutality());
        add(new Combust());
        add(new Corruption());
        add(new DarkEmbrace());
        add(new LimitBreak());
        add(new Metallicize());
        add(new ThunderClap());
        add(new Bludgeon());
        add(new Bash());
        add(new Uppercut());
        add(new SearingBlow());
        add(new Reaper());
        add(new Rampage());
        add(new TwinStrike());
        add(new PommelStrike());
        add(new HeavyBlade());
        add(new WildStrike());
        add(new PerfectedStrike());
        add(new Carnage());
        add(new InfernalBlade());
        add(new Immolate());
        add(new Headbutt());
        add(new Feed());
        add(new IronWave());
        add(new FiendFire());
        add(new Dropkick());
        add(new Hemokinesis());
        add(new Clothesline());
        add(new Cleave());
        add(new BloodForBlood());
        add(new Pummel());
        add(new RecklessCharge());
        add(new SeverSoul());
        add(new SwordBoomerang());

        add(new Anger());

        // Block last
        add(new SeeingRed());
        add(new ShrugItOff());
        add(new GhostlyArmor());
        add(new FlameBarrier());
        add(new Disarm());
        add(new PowerThrough());
        add(new Impervious());
        add(new BurningPact());
        add(new SecondWind());
        add(new TrueGrit());

        add(new Entrench());
        add(new BodySlam());

        add(new Exhume());

        add(new Havoc());
        add(new Warcry());


        return uglyThing;
    }

    private static void add(AbstractCard card) {
        uglyThing.put(card.cardID, uglyThing.size());
    }
}
