package io.astraeus.net.packet.in;

import io.astraeus.game.event.impl.NpcSecondClickEvent;
import io.astraeus.game.task.impl.DistancedTask;
import io.astraeus.game.world.World;
import io.astraeus.game.world.entity.mob.npc.Npc;
import io.astraeus.game.world.entity.mob.player.Player;
import io.astraeus.net.codec.ByteModification;
import io.astraeus.net.codec.ByteOrder;
import io.astraeus.net.packet.IncomingPacket;
import io.astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode({IncomingPacket.NPC_OPTION_2})
public final class NpcSecondClickPacket implements Receivable {

  @Override
  public void handlePacket(Player player, IncomingPacket packet) {
    final Npc npc = World.getNpcs()
        .get(packet.getReader().readShort(ByteOrder.LITTLE, ByteModification.ADDITION));

    if (npc == null) {
      return;
    }

    player.startAction(new DistancedTask(player, npc.getPosition(), 2) {

      @Override
      public void onReached() {
        player.setInteractingEntity(npc);
        npc.setInteractingEntity(player);
        player.post(new NpcSecondClickEvent(npc));
        stop();

      }

    });

  }

}
