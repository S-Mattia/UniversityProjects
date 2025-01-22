CFLAGS =-Wvla -Wextra -Werror
COMMON_OBJ = bin/utils.o bin/ini.o

all: atom inhibitor feeder activator master

master: $(COMMON_OBJ)
	gcc $(CFLAGS) ./src/master.c $(COMMON_OBJ) -o bin/master

inhibitor: $(COMMON_OBJ)
	gcc $(CFLAGS) ./src/inhibitor.c $(COMMON_OBJ) -o bin/inhibitor

feeder: $(COMMON_OBJ)
	gcc $(CFLAGS) ./src/feeder.c $(COMMON_OBJ) -o bin/feeder

activator: $(COMMON_OBJ)
	gcc $(CFLAGS) ./src/activator.c $(COMMON_OBJ) -o bin/activator

atom: $(COMMON_OBJ)
	gcc $(CFLAGS) ./src/atom.c $(COMMON_OBJ) -o bin/atom

# rules for common
bin/utils.o: ./src/utils.c
	gcc $(CFLAGS) -c ./src/utils.c -o bin/utils.o

bin/ini.o: ./library/ini.c
	gcc $(CFLAGS) -c ./library/ini.c -o bin/ini.o


clean:
	rm -f bin/*
	rm -f log/*


