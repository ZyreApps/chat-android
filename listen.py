import pyre
import zmq
import signal
import sys

# Set node name and group
node_name = "pyrelisten"
node_group = "testgroup"
if len(sys.argv) > 1:
    node_name = sys.argv[1]
if len(sys.argv) > 2:
    node_group = sys.argv[2]

node = pyre.Pyre(node_name)
node.join(node_group)
node.start()
sock = node.inbox

print("Sock is")
print(sock)

signal.signal(signal.SIGINT, signal.SIG_DFL) # Fixes Ctrl-C

# Poll for messages and print them
poller = zmq.Poller()
poller.register(sock, zmq.POLLIN)

while True:
    items = dict(poller.poll(100))
    if sock in items and items[sock] == zmq.POLLIN:
        message_parts = node.recv()
        message_kind = message_parts[0]

        if message_kind in ["SHOUT", "WHISPER"]:
            message_addr = message_parts[1]
            message_sender = message_parts[2]
            message_group = message_parts[3]
            message_body = message_parts[4]
            message_str = " ".join([
                message_sender, message_group, message_body
            ])
            print("[%s] %s" % (message_kind, message_str))

        elif message_kind in ["ENTER", "EXIT"]:
            message_sender = message_parts[2]
            print("[%s] %s" % (message_kind, message_sender))

        elif message_kind == "JOIN":
            message_sender = message_parts[2]
            message_group = message_parts[3]
            message_str = " ".join([
                message_sender, message_group
            ])
            print("[%s] %s" % (message_kind, message_str))

        else:
            print(message_parts)

