package io.papermc.paper.entity.temptation;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.armadillo.ArmadilloAi;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.entity.animal.camel.CamelAi;
import net.minecraft.world.entity.animal.frog.FrogAi;
import net.minecraft.world.entity.animal.goat.GoatAi;
import net.minecraft.world.entity.animal.sniffer.SnifferAi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * The tempt state lookup holds onto cached temptation flags of players in the world.
 */
public class GlobalTemptationLookup {
    private static int registeredPredicateCounter = 0;

    public static final TemptationPredicate BEE_FOOD = register(stack -> stack.is(ItemTags.BEE_FOOD));
    public static final TemptationPredicate CHICKEN_FOOD = register(stack -> stack.is(ItemTags.CHICKEN_FOOD));
    public static final TemptationPredicate COW_FOOD = register(stack -> stack.is(ItemTags.COW_FOOD));
    public static final TemptationPredicate PANDA_FOOD = register(stack -> stack.is(ItemTags.PANDA_FOOD));
    public static final TemptationPredicate PIG_CARROT_ON_A_STICK = register(stack -> stack.is(Items.CARROT_ON_A_STICK));
    public static final TemptationPredicate PIG = register(stack -> stack.is(ItemTags.PIG_FOOD));
    public static final TemptationPredicate RABBIT_FOOD = register(stack -> stack.is(ItemTags.RABBIT_FOOD));
    public static final TemptationPredicate SHEEP_FOOD = register(stack -> stack.is(ItemTags.SHEEP_FOOD));
    public static final TemptationPredicate TURTLE_FOOD = register(stack -> stack.is(ItemTags.TURTLE_FOOD));
    public static final TemptationPredicate HORSE_FOOD = register(stack -> stack.is(ItemTags.HORSE_TEMPT_ITEMS));
    public static final TemptationPredicate LLAMA_TEMPT_ITEMS = register(stack -> stack.is(ItemTags.LLAMA_TEMPT_ITEMS));
    public static final TemptationPredicate STRIDER_TEMPT_ITEMS = register(stack -> stack.is(ItemTags.STRIDER_TEMPT_ITEMS));
    public static final TemptationPredicate CAT_FOOD = register(stack -> stack.is(ItemTags.CAT_FOOD));
    public static final TemptationPredicate OCELOT_FOOD = register(itemStack -> itemStack.is(ItemTags.OCELOT_FOOD));
    public static final TemptationPredicate AXOLOTL_TEMPTATIONS = register(AxolotlAi.getTemptations());
    public static final TemptationPredicate GOAT_TEMPTATIONS = register(GoatAi.getTemptations());
    public static final TemptationPredicate FROG_TEMPTATIONS = register(FrogAi.getTemptations());
    public static final TemptationPredicate CAMEL_TEMPTATIONS = register(CamelAi.getTemptations());
    public static final TemptationPredicate ARMADILLO_TEMPTATIONS = register(ArmadilloAi.getTemptations());
    public static final TemptationPredicate SNIFFER_TEMPTATIONS = register(SnifferAi.getTemptations());

    public record TemptationPredicate(int index, Predicate<ItemStack> predicate) implements Predicate<ItemStack> {

        @Override
        public boolean test(final ItemStack itemStack) {
            return this.predicate.test(itemStack);
        }
    }

    public static int indexFor(final Predicate<ItemStack> predicate) {
        return predicate instanceof final TemptationPredicate temptationPredicate ? temptationPredicate.index() : -1;
    }

    private static TemptationPredicate register(final Predicate<ItemStack> predicate) {
        final TemptationPredicate val = new TemptationPredicate(registeredPredicateCounter, predicate);
        registeredPredicateCounter++;
        return val;
    }

    private final List<BitSet> precalculatedTemptItems = new ArrayList<>();
    private final BitSet calculatedThisTick = new BitSet();

    {
        for (int i = 0; i < registeredPredicateCounter; i++) {
            this.precalculatedTemptItems.add(new BitSet());
        }
    }

    public void reset() {
        for (int i = 0; i < registeredPredicateCounter; i++) {
            this.precalculatedTemptItems.get(i).clear();
        }
        this.calculatedThisTick.clear();
    }

    public boolean isCalculated(final int index) {
        return this.calculatedThisTick.get(index);
    }

    public void setCalculated(final int index) {
        this.calculatedThisTick.set(index);
    }

    public BitSet getBitSet(final int index) {
        return this.precalculatedTemptItems.get(index);
    }
}
