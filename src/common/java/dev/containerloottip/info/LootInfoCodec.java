package dev.containerloottip.info;

import dev.containerloottip.ContainerLootTip;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Manual buffer codec for {@link ContainerLootInfo}.
 *
 * <p>Written by hand instead of using a {@code StreamCodec} because 1.20.1 Forge and 1.21.1 NeoForge
 * use different payload wrapper APIs; only the raw buffer operations are shared.</p>
 */
public final class LootInfoCodec {
    private LootInfoCodec() {
    }

    public static void write(FriendlyByteBuf buffer, ContainerLootInfo info) {
        buffer.writeVarInt(info.menuId());
        buffer.writeResourceLocation(info.tableId());
        buffer.writeVarInt(info.source().ordinal());
        buffer.writeVarInt(info.kind().ordinal());
        buffer.writeVarInt(info.size());
        buffer.writeLong(info.seed());
        buffer.writeBoolean(info.openedBefore());
        buffer.writeVarInt(info.openers());
        buffer.writeVarInt(info.refreshTicks());
        buffer.writeVarInt(info.decayTicks());
    }

    public static ContainerLootInfo read(FriendlyByteBuf buffer) {
        int menuId = buffer.readVarInt();
        ResourceLocation tableId = buffer.readResourceLocation();
        LootSource source = readEnum(buffer, LootSource.values(), LootSource.VANILLA, "source");
        ContainerKind kind = readEnum(buffer, ContainerKind.values(), ContainerKind.OTHER, "kind");
        int size = buffer.readVarInt();
        long seed = buffer.readLong();
        boolean openedBefore = buffer.readBoolean();
        int openers = buffer.readVarInt();
        int refreshTicks = buffer.readVarInt();
        int decayTicks = buffer.readVarInt();
        return new ContainerLootInfo(menuId, tableId, source, kind, size, seed, openedBefore, openers, refreshTicks, decayTicks);
    }

    private static <T> T readEnum(FriendlyByteBuf buffer, T[] values, T fallback, String name) {
        int ordinal = buffer.readVarInt();

        if (ordinal < 0 || ordinal >= values.length) {
            ContainerLootTip.LOGGER.warn("Received invalid loot info {} ordinal {}", name, ordinal);
            return fallback;
        }

        return values[ordinal];
    }
}
