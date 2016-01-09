import pyre
import sys
import time

# Set node name and group
node_name = "pyresend"
node_group = "testgroup"
if len(sys.argv) > 1:
    node_name = sys.argv[1]
if len(sys.argv) > 2:
    node_group = sys.argv[2]

node = pyre.Pyre(node_name)
node.start()
node.join(node_group)

run = True
while run:
    got = raw_input("> ")
    if got == "QUIT":
        run = False
        node.stop()
        time.sleep(1)
        sys.exit()
    else:
        node.shout(node_group, got)
