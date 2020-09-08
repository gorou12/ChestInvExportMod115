package dev.pokete.chestinvexport;

import com.google.gson.Gson;
import dev.pokete.chestinvexport.model.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static dev.pokete.chestinvexport.ChestInvExportUtil.getRepairTimes;

@Mod("chestinvexportmod")
public class ChestInvExportMod {
    private List<ItemStack> itemStackList = null;
    private ItemStack keepItemStack = null;
    private boolean recordingMode = false;

    public ChestInvExportMod() {
        // MinecraftForge.EVENT_BUS で処理するイベントのハンドラメソッドを持つオブジェクト(this)を登録
        MinecraftForge.EVENT_BUS.register(this);
        KeybindHandler.init();
    }

    @SubscribeEvent
    public void onItemFocus(ItemTooltipEvent e) {
        // RecordingModeじゃなければ抜ける
        if (!recordingMode) return;

        ItemStack itemStack = e.getItemStack();

        // 前と同じItemStackなら無視（tickごとに拾っちゃうため）
        if (keepItemStack != null && keepItemStack.equals(itemStack)) return;

        keepItemStack = itemStack;
        if (itemStack.getItem().getRegistryName() == null || itemStack.getTag() == null) return;
        if (itemStack.getTag().get("StoredEnchantments") == null && !isEcoEgg(itemStack)) return;
        itemStackList.add(itemStack);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if (KeybindHandler.record_key.isKeyDown()) {
            onPressRecordKey();
        } else if (KeybindHandler.quit_key.isKeyDown()) {
            onPressQuitKey();
        }
    }

    private void onPressQuitKey(){
        if(recordingMode){
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            ClientPlayerEntity me = mc.player;

            recordingMode = false;
            me.sendMessage(new StringTextComponent(I18n.format("chest_inv_export.quit_recording")));
        }
    }

    private void onPressRecordKey() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        ClientPlayerEntity me = mc.player;

        if (recordingMode) {
            // on -> off
            recordingMode = false;
            finishRecording(me);
        } else {
            // off -> on
            recordingMode = true;
            startRecording(me);
        }
    }

    private void startRecording(ClientPlayerEntity me) {
        itemStackList = new ArrayList<>();
        me.sendMessage(new StringTextComponent(I18n.format("chest_inv_export.start_recording")));
    }

    private void finishRecording(ClientPlayerEntity me) {
        me.sendMessage(new StringTextComponent(I18n.format("chest_inv_export.finish_recording")));
        if (itemStackList.size() == 0) {
            me.sendMessage(new StringTextComponent(I18n.format("chest_inv_export.nothing_to_export")));
            return;
        }
        BooksData bd = new BooksData();
        List<Book> books = new ArrayList<>();

        for (ItemStack itemStack : itemStackList) {
            if (itemStack.getItem().getRegistryName() == null || itemStack.getTag() == null) return;

            Book book = new Book();

            switch (itemStack.getItem().getRegistryName().toString()) {
                case "minecraft:enchanted_book":
                    book.setItemType("enchanted_book");
                    List<Enchantment> enchantments = new ArrayList<>();

                    ListNBT listnbt = itemStack.getTag().getList("StoredEnchantments", Constants.NBT.TAG_COMPOUND);
                    for (INBT inbt : listnbt) {
                        if (!(inbt instanceof CompoundNBT)) continue;
                        Enchantment enchant = new Enchantment();
                        CompoundNBT cnbt = (CompoundNBT) inbt;
                        enchant.setEnchantment(cnbt.getString("id"));
                        enchant.setLevel((int) cnbt.getShort("lvl"));
                        enchantments.add(enchant);
                    }

                    book.setRepairTimes(getRepairTimes(itemStack.getRepairCost()));
                    book.setEnchantments(enchantments);

                    break;
                case "minecraft:written_book":
                    book.setItemType("eco_egg");
                    book.setCount(itemStack.getCount());
                    break;
                default:
                    return;
            }

            books.add(book);
        }

        bd.setBooks(books);
        Date date = new Date();
        SimpleDateFormat file_ts = new SimpleDateFormat("yyMMdd_HHmmss_SSS");
        SimpleDateFormat content_ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filePath = "D:\\Minecraft\\ChestInvExport\\";
        File file = new File(filePath + file_ts.format(date) + ".json");
        bd.setCreatedDate(content_ts.format(date));

        Gson gson = new Gson();
        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(bd, fw);
        } catch (IOException ioException) {
            System.out.println(Arrays.toString(ioException.getStackTrace()));
            me.sendMessage(new StringTextComponent(I18n.format("chest_inv_export.failure_saving")));
        }
        me.sendMessage(new StringTextComponent(I18n.format("chest_inv_export.successful_saving", file.getAbsolutePath())));
    }

    private boolean isEcoEgg(ItemStack itemStack) {
        return (itemStack.getItem().getRegistryName() != null)
        && ("minecraft:written_book".equals(itemStack.getItem().getRegistryName().toString()))
        && (itemStack.getTag() != null)
        && ("魔道書「えこたまご」".equals(itemStack.getTag().getString("title")))
        && ("神官えこ".equals(itemStack.getTag().getString("author")));
    }
}
