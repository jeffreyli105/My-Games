CXX       := g++-14
CXXFLAGS  := -std=c++20 -Wall -Werror=vla -MMD
EXEC      := chess
OBJECTS = subject.o main.o grid.o textdisplay.o cell.o graphicsdisplay.o window.o

SRCS      := $(wildcard *.cc)
OBJECTS   := $(SRCS:.cc=.o)
DEPENDS   := $(OBJECTS:.o=.d)

${EXEC}: ${OBJECTS}
	${CXX} ${CXXFLAGS} ${OBJECTS} -o ${EXEC} -lX11

-include ${DEPENDS}

.PHONY: clean

clean:
	rm ${OBJECTS} ${EXEC} ${DEPENDS}
