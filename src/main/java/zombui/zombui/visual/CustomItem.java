package zombui.zombui.visual;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomItem {

    /**
     * ItemStack to be used
     */
    private ItemStack itemStack;

    /**
     * Constructor for Custom Items
     * @param paramMaterial Material to be used
     */
    public CustomItem(Material paramMaterial) {
        this(new ItemStack(paramMaterial));
    }

    /**
     * Constructor for Custom Items
     * @param paramMaterial Material to be used
     * @param itemName Itemname to be used
     */
    public CustomItem(Material paramMaterial, String itemName) {
        this(new ItemStack(paramMaterial));
        name(itemName);
    }

    /**
     * Constructor for Custom Items
     * @param paramItemStack ItemStack to use
     */
    public CustomItem(ItemStack paramItemStack) {
        this(paramItemStack, false);
    }

    /**
     * Constructor for Custom Items
     * @param paramItemStack ItemStack to use
     * @param hideAttributes of ItemStack
     */
    public CustomItem(ItemStack paramItemStack, boolean hideAttributes) {
        Preconditions.checkArgument((paramItemStack != null), "ItemStack cannot be null");
        this.itemStack = paramItemStack.clone();
        if (hideAttributes)
            showAllAttributes(false);
    }

    /**
     * Method to convert Numbers
     * @param paramObject to convert
     * @return converted Object as Number
     */
    private static Number convertNumber(Object paramObject) {
        Number number = (Number) paramObject;
        if (number instanceof Long) {
            Long long_ = (Long) number;
            if (long_.longValue() == long_.intValue())
                return Integer.valueOf(long_.intValue());
        }
        if (number instanceof Float) {
            Float float_ = (Float) number;
            if (float_.floatValue() == float_.intValue())
                return Integer.valueOf(float_.intValue());
        }
        if (number instanceof Double) {
            Double double_ = (Double) number;
            if (double_.doubleValue() == double_.intValue())
                return Integer.valueOf(double_.intValue());
        }
        return number;
    }

    /**
     * Change Material of ItemStack
     * @param paramMaterial to be used
     * @return changed instance
     */
    public CustomItem material(Material paramMaterial) {
        this.itemStack.setType(paramMaterial);
        return this;
    }

    /**
     * Change this to Skull item
     * @param paramString Name of Skull owner
     * @return changed instance
     */
    public CustomItem skull(String paramString) {
        if (this.itemStack.getItemMeta() != null && this.itemStack.getItemMeta() instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayerIfCached(paramString));
            this.itemStack.setItemMeta(skullMeta);
        }
        return this;
    }

    public CustomItem name() {
        return name("§r");
    }

    /**
     * Change name of item
     * @param paramString new name
     * @return changed instance
     */
    public CustomItem name(String paramString) {
        if (this.itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            itemMeta.displayName(Component.text(Text.color(paramString)));
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    /**
     * Change amount
     * @param paramInt new amount
     * @return changed instance
     */
    public CustomItem amount(int paramInt) {
        this.itemStack.setAmount(paramInt);
        return this;
    }

    /**
     * Change Lore of Item
     * @param paramVarArgs new item lore
     * @return changed instance
     */
    public CustomItem lore(String... paramVarArgs) {
        lore(Arrays.asList(paramVarArgs));
        return this;
    }

    /**
     * Change Lore of Item
     * @param paramList new item lore
     * @return changed instance
     */
    public CustomItem lore(List<String> paramList) {
        if (this.itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            List<Component> components = new ArrayList<>();
            for (String s : paramList) {
                components.add(Component.text(s));
            }
            itemMeta.lore(components);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    /**
     * Add enchantment to ItemStack
     * @param paramEnchantment to add
     * @param paramInt level as int
     * @return changed instance
     */
    public CustomItem addEnchantment(Enchantment paramEnchantment, int paramInt) {
        this.itemStack.addUnsafeEnchantment(paramEnchantment, paramInt);
        return this;
    }

    /**
     * Add enchantment to ItemStack
     * @param paramEnchantment to add
     * @return changed instance
     */
    public CustomItem removeEnchantment(Enchantment paramEnchantment) {
        this.itemStack.removeEnchantment(paramEnchantment);
        return this;
    }

    /**
     * Append to lore at start
     * @param paramVarArgs new start
     * @return changed instance
     */
    public CustomItem appendLoreBeginning(String... paramVarArgs) {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(paramVarArgs));
        arrayList.addAll(getStringLore());
        lore(arrayList);
        return this;
    }

    /**
     * Append to item lore
     * @param paramList what to append
     * @return changed instance
     */
    public CustomItem appendLore(List<String> paramList) {
        return appendLore(paramList.toArray(new String[0]));
    }

    /**
     * Append to item lore
     * @param paramVarArgs what to append
     * @return changed instance
     */
    public CustomItem appendLore(String... paramVarArgs) {
        ArrayList<String> arrayList = new ArrayList<>(getStringLore());
        arrayList.addAll(Arrays.asList(paramVarArgs));
        lore(arrayList);
        return this;
    }

    /**
     * Get material from bukkit
     * @return material
     */
    public Material getBukkitMaterial() {
        return this.itemStack.getType();
    }

    /**
     * Get name of item
     * @return name as string
     */
    public String getName() {
        return (this.itemStack.getItemMeta() != null) ? this.itemStack.getItemMeta().getDisplayName() : null;
    }

    /**
     * get item amount
     * @return item amount as int
     */
    public int getAmount() {
        return this.itemStack.getAmount();
    }

    /**
     * Get lore as arraylist
     * @return Lore as component arraylist
     */
    public ArrayList<Component> getLore() {
        return (this.itemStack.getItemMeta() != null && this.itemStack.getItemMeta().hasLore()) ? new ArrayList<>(this.itemStack.getItemMeta().lore()) : new ArrayList<>();
    }

    /**
     * Get lore as arraylist
     * @return Lore as string arraylist
     */
    public ArrayList<String> getStringLore() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Component component : getLore()) {
            arrayList.add(component.insertion());
        }
        return arrayList;
    }

    /**
     * Get enchantments as map
     * @return enchantments as map
     */
    public Map<Enchantment, Integer> getEnchants() {
        return this.itemStack.getEnchantments();
    }

    public CustomItem showEnchantments(boolean paramBoolean) {
        if (this.itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            if (paramBoolean) {
                itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public CustomItem showAllAttributes(boolean paramBoolean) {
        if (this.itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            if (paramBoolean) {
                itemMeta.removeItemFlags(ItemFlag.values());
            } else {
                itemMeta.addItemFlags(ItemFlag.values());
            }
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    /**
     * Clone custom item
     * @return new copy of custom item
     */
    public CustomItem clone() {
        return new CustomItem(this.itemStack.clone(), false);
    }

    /**
     * Clone custom item
     * @return new copy of custom item
     */
    public ItemStack get() {
        return this.itemStack.clone();
    }

    public void setBukkitStack(ItemStack paramItemStack) {
        this.itemStack = paramItemStack;
    }
}
