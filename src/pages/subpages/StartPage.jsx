import SectionHead from "../../components/SectionHead";
import Title from "../../components/Title";
import Code from "../../components/Code";
import LargeText from "../../components/LargeText";
import List from "../../components/List";
import Tabs from "../../components/Tabs";

export default function StartPage() {
  return (
    <>
      <Title>Getting Started</Title>
      <Tabs pages={["Beginner", "Intermediate", "Advanced"]}>
        <BeginnersPage />
        <IntermediatePage />
        <AdvancedPage />
      </Tabs>
    </>
  );
}

function BeginnersPage() {
  return (
    <section>
      <SectionHead className="mt-[20px] mb-[30px]">
        First Time Programming
      </SectionHead>
      <LargeText className="my-[10px]">Writing to console</LargeText>
      <div className="m-[10px]">
        Lets start with the following piece of code:
        <Code blocked colored>
          io write("Hello, World!")
        </Code>
        This will display <Code colored={false}>Hello, World!</Code> to the
        console. <br />
        It may seem a bit more verbose than the print statement of some other
        languages, but the unified <Code>read</Code> and <Code>write</Code>{" "}
        functions will work more intuitively with files.
      </div>
      <LargeText className="my-[10px]">Constants and Variables</LargeText>
      <div className="m-[10px]">
        Now lets replace the above code with the following:
        <Code blocked colored>
          message = "Hello, World!" <br />
          io write(message)
        </Code>
        This will still display <Code colored={false}>Hello, World!</Code>, but
        it allows for more flexibility, as the constant <Code>message</Code>{" "}
        will likely be used more than this one time. <br />
        Constants and variables also make swapping out values easier. For
        example, we can replace the above code with the following:
        <Code blocked colored>
          message = "Hello, You!" <br />
          io write(message)
        </Code>
        or even
        <Code blocked colored>
          message = 25.8069758011
          <br />
          io write(message)
        </Code>
        Sometimes, there will be a need to modify an existing variable. Let's
        see the example below:
        <Code blocked colored>
          path = ["up", "up", "left", "down"] <br /> <br />
          x, y = var: 0, 0<br />
          for direction in path:
          <br />
          &emsp; &emsp; if direction == "up": ++y;
          <br />
          &emsp; &emsp; else direction == "down": --y;
          <br />
          &emsp; &emsp; else direction == "right": ++x;
          <br />
          &emsp; &emsp; else direction == "left": --x;
        </Code>
        There are a couple of things to explain here.
        <List>
          <span>
            First, note that lists are rarely initialized by hand, as in, the
            bracket (<Code>[]</Code>) enclosed sequence of directions will
            rarely be hand-typed
          </span>
          <span>
            The keyword <Code>var</Code> in the assignment of <Code>x</Code> and{" "}
            <Code>y</Code> signals that they are variables
          </span>
          <span>
            The <Code>for</Code> loop creates the constant{" "}
            <Code>direction</Code> for every value in path
          </span>
          <span>
            The <Code>if</Code>-<Code>else</Code> statement is tabbed over by a
            single tab (equivalent to 4 spaces) to signal that they fall under
            the <Code>for</Code> loop
          </span>
          <span>
            A colon (<Code>:</Code>) follows the condition of the{" "}
            <Code>for</Code> loop and <Code>if</Code> and <Code>else</Code>{" "}
            statements
          </span>
          <span>
            The increment (<Code>++</Code>) and decrement (<Code>--</Code>)
            modifies the the <Code>x</Code> and <Code>y</Code> variables by
            adding 1 or subtracting 1 to each.
          </span>
        </List>
        Variables are needed less frequently than constants, so the default is
        constant.
      </div>
      <LargeText className="my-[10px]">Conditions and Loops</LargeText>
      <div className="m-[10px]">
        We mentioned the <Code>for</Code> loop and <Code>if</Code> and{" "}
        <Code>else</Code> statements above, we will now look at all the
        different types of loops:
        <Code blocked colored>
          n = 102 <br />
          if n % 2 == 0: <br />
          &emsp; &emsp; io write("even\n") <br />
          else n % 3 == 0: <br />
          &emsp; &emsp; io write("divisible by 3\n") <br />
          all: io write("divisible by 6\n") <br />
          any: io write("divisible by 2 or 3\n") <br />
          else: io write("not divisible by 2 or 3")
        </Code>
        The <Code>if</Code>-<Code>else</Code>-<Code>any</Code>-<Code>all</Code>{" "}
        statement should not be abused.
        <br />
        <Code blocked colored>
          grid = [repeat 10: [repeat 10: 0]] \\creates a 10x10 grid of 0s
          <br />
          repeat: <br />
          &emsp; &emsp; if random([0...100)) as int == 0: break
        </Code>
        The most common use of the <Code>repeat</Code> loop is to initialize
        lists to a fixed size and for unconditional repetition. <br />
        The <Code>break</Code> statement will terminate the most recent loop.
        <br />
        <Code blocked colored>
          while (input = io read()) then input lowercase != "q": <br />
          &emsp; &emsp; io write(input)
        </Code>
        The most common use of the <Code>while</Code> loop is to await a
        condition.
        <br />
        Constants declared in the condition of the while loop will be accessible
        in the condition and the body of the loop for every pass of the loop.
        <br />
        <Code blocked colored>
          for i in [0...10): <br />
          &emsp; &emsp; io write("*" * 10) \\prints a 10x10 block of stars
        </Code>
        The for loop is the most commonly used loop. It can be used to repeat a
        block of code a fixed number of times, to use every value of an array,
        and to test every number of a range.
        <br />
        Constants declared in the condition of the for loop (before the{" "}
        <Code>in</Code> operator) will be accessible in the body of the loop for
        every pass of the loop.
        <br />
        <br />
      </div>
      <LargeText className="my-[10px]">Functions</LargeText>
      <div className="m-[10px]">
        Breaking the code into smaller pieces makes it easier to write and
        change the program. The following is an example of a function:
        <Code blocked colored>
          pow4(n) = n * n * n * n <br />
          io write(pow4(1)) \\1 <br />
          io write(pow4(2)) \\8 <br />
          io write(pow4(3)) \\81
        </Code>
        The basic structure is <Code>name(parameters) = code</Code>
        <Code blocked colored>
          piglatin(s = string) = &#123;return ret = string&#125; : &#123;
          <br />
          &emsp; &emsp; ret = var: s[[1 ... s length)]
          <br />
          &emsp; &emsp; ret += " "
          <br />
          &emsp; &emsp; ret += s[0] + "ay"
          <br />
          &#125;
          <br />
          io write(piglatin("hello")) \\ello hey <br />
          io write(piglatin("banana")) \\anana bay
        </Code>
        The function can be multiline. And if there are many lines of code,
        using a brace (<Code>&#123;&#125;</Code>) enclosed block with a{" "}
        <Code>return</Code>{" "}
        <Code blocked colored>
          sqrt(n = float) = &#123;positive, negative = int, int&#125; : &#123;
          <br />
          &emsp; &emsp; positive = n */ 2
          <br />
          &emsp; &emsp; negative = - n */ 2
          <br />
          &#125;
          <br />
          <br />
          &#123; a = positive, b = negative &#125; = root16 = sqrt(16) <br />
          <br />
          io write(a) \\4 <br />
          io write(root16 negative) \\-4
        </Code>
        This is a combination of the type-conversion and the direct
        function-value syntax. This makes it easy to return multiple values.{" "}
        <br />
        Destructuring isn't necessary, but it makes this relatively easy to use.
      </div>
    </section>
  );
}

function IntermediatePage() {
  return (
    <section>
      <SectionHead className="mt-[20px] mb-[30px]">
        Intermediate Programming
      </SectionHead>
      <LargeText className="my-[10px]">Writing to console</LargeText>
      <div className="m-[10px]">
        The <Code>io</Code> module supprts all forms of input/output:
        <Code blocked colored>
          io write("Hello, World!") <br />
          input = io read() <br />
          <br />
          file = io: uri: "example.txt" <br />
          file write("file content") <br />
          <br />
          file = io: uri: "example2.txt" <br />
          l = file read&#123;until = '\n'&#125;() \\gets the first line
          <br />
          s = file read&#123;count = 1&#125;() \\gets the next character as a
          string <br />c = file get() \\gets the next character as a char
        </Code>
        The <Code>io</Code> module is routed to the console by default, but an
        instance of it for files can be created by providing a uri
      </div>
      <LargeText className="my-[10px]">Constants and Variables</LargeText>
      <div className="m-[10px]">
        Identifiers are constant by default, if it is intended to be modified,
        its type will need to specify <Code>var</Code> via the colon (
        <Code>:</Code>):
        <Code blocked colored>
          path = ["up", "up", "left", "down"] <br /> <br />
          x, y = var: 0, 0<br />
          for direction in path:
          <br />
          &emsp; &emsp; if direction == "up": ++y;
          <br />
          &emsp; &emsp; else direction == "down": --y;
          <br />
          &emsp; &emsp; else direction == "right": ++x;
          <br />
          &emsp; &emsp; else direction == "left": --x;
        </Code>
      </div>
      <LargeText className="my-[10px]">Conditions and Loops</LargeText>
      <div className="m-[10px]">
        We mentioned the <Code>for</Code> loop and <Code>if</Code> and{" "}
        <Code>else</Code> statements above, we will now look at all the
        different types of loops:
        <Code blocked colored>
          n = 102 <br />
          if n % 2 == 0: <br />
          &emsp; &emsp; io write("even\n") <br />
          else n % 3 == 0: <br />
          &emsp; &emsp; io write("divisible by 3\n") <br />
          all: io write("divisible by 6\n") <br />
          any: io write("divisible by 2 or 3\n") <br />
          else: io write("not divisible by 2 or 3")
        </Code>
        The <Code>if</Code>-<Code>else</Code>-<Code>any</Code>-<Code>all</Code>{" "}
        statement should not be abused.
        <br />
        <Code blocked colored>
          grid = [repeat 10: [repeat 10: 0]] \\creates a 10x10 grid of 0s
          <br />
          repeat: <br />
          &emsp; &emsp; if random([0...100)) as int == 0: break
        </Code>
        The most common use of the <Code>repeat</Code> loop is to initialize
        lists to a fixed size and for unconditional repetition. <br />
        The <Code>break</Code> statement will terminate the most recent loop.
        <br />
        <Code blocked colored>
          while (input = io read()) then input lowercase != "q": <br />
          &emsp; &emsp; io write(input)
        </Code>
        The most common use of the <Code>while</Code> loop is to await a
        condition.
        <br />
        Constants declared in the condition of the while loop will be accessible
        in the condition and the body of the loop for every pass of the loop.
        <br />
        <Code blocked colored>
          for i in [0...10): <br />
          &emsp; &emsp; io write("*" * 10) \\prints a 10x10 block of stars
        </Code>
        The for loop is the most commonly used loop. It can be used to repeat a
        block of code a fixed number of times, to use every value of an array,
        and to test every number of a range.
        <br />
        Constants declared in the condition of the for loop (before the{" "}
        <Code>in</Code> operator) will be accessible in the body of the loop for
        every pass of the loop.
        <br />
        <br />
      </div>
      <LargeText className="my-[10px]">Functions</LargeText>
      <div className="m-[10px]">
        Breaking the code into smaller pieces makes it easier to write and
        change the program. The following is an example of a function:
        <Code blocked colored>
          pow4(n) = n * n * n * n <br />
          io write(pow4(1)) \\1 <br />
          io write(pow4(2)) \\8 <br />
          io write(pow4(3)) \\81
        </Code>
        The basic structure is <Code>name(parameters) = code</Code>
        <Code blocked colored>
          piglatin(s = string) = &#123;return ret = string&#125; : &#123;
          <br />
          &emsp; &emsp; ret = var: s[[1 ... s length)]
          <br />
          &emsp; &emsp; ret += " "
          <br />
          &emsp; &emsp; ret += s[0] + "ay"
          <br />
          &#125;
          <br />
          io write(piglatin("hello")) \\ello hey <br />
          io write(piglatin("banana")) \\anana bay
        </Code>
        The function can be multiline. And if there are many lines of code,
        using a brace (<Code>&#123;&#125;</Code>) enclosed block with a{" "}
        <Code>return</Code>{" "}
        <Code blocked colored>
          sqrt(n = float) = &#123;positive, negative = int, int&#125; : &#123;
          <br />
          &emsp; &emsp; positive = n */ 2
          <br />
          &emsp; &emsp; negative = - n */ 2
          <br />
          &#125;
          <br />
          <br />
          &#123; a = positive, b = negative &#125; = root16 = sqrt(16) <br />
          <br />
          io write(a) \\4 <br />
          io write(root16 negative) \\-4
        </Code>
        This is a combination of the type-conversion and the direct
        function-value syntax. This makes it easy to return multiple values.{" "}
        <br />
        Destructuring isn't necessary, but it makes this relatively easy to use.
      </div>
    </section>
  );
}

function AdvancedPage() {
  return (
    <section>
      <SectionHead className="mt-[20px] mb-[30px]">
        Advanced Programming
      </SectionHead>
      <LargeText className="my-[10px]">Writing to console</LargeText>
      <div className="m-[10px]">
        The <Code>io</Code> module supprts all forms of input/output:
        <Code blocked colored>
          io write("Hello, World!") <br />
          input = io read() <br />
          <br />
          file = io: uri: "example.txt" <br />
          file write("file content") <br />
          <br />
          file = io: uri: "example2.txt" <br />
          l = file read&#123;until = '\n'&#125;() \\gets the first line
          <br />
          s = file read&#123;count = 1&#125;() \\gets the next character as a
          string <br />c = file get() \\gets the next character as a char
        </Code>
        The <Code>io</Code> module is routed to the console by default, but an
        instance of it for files can be created by providing a uri
      </div>
      <LargeText className="my-[10px]">Constants and Variables</LargeText>
      <div className="m-[10px]">
        Identifiers are constant by default, if it is intended to be modified,
        its type will need to specify <Code>var</Code>:
        <Code blocked colored>
          path = ["up", "up", "left", "down"] <br /> <br />
          x, y = var: 0, 0<br />
          for direction in path:
          <br />
          &emsp; &emsp; if direction == "up": ++y;
          <br />
          &emsp; &emsp; else direction == "down": --y;
          <br />
          &emsp; &emsp; else direction == "right": ++x;
          <br />
          &emsp; &emsp; else direction == "left": --x;
        </Code>
      </div>
      <LargeText className="my-[10px]">Conditions and Loops</LargeText>
      <div className="m-[10px]">
        We mentioned the <Code>for</Code> loop and <Code>if</Code> and{" "}
        <Code>else</Code> statements above, we will now look at all the
        different types of loops:
        <Code blocked colored>
          n = 102 <br />
          if n % 2 == 0: <br />
          &emsp; &emsp; io write("even\n") <br />
          else n % 3 == 0: <br />
          &emsp; &emsp; io write("divisible by 3\n") <br />
          all: io write("divisible by 6\n") <br />
          any: io write("divisible by 2 or 3\n") <br />
          else: io write("not divisible by 2 or 3")
        </Code>
        The <Code>if</Code>-<Code>else</Code>-<Code>any</Code>-<Code>all</Code>{" "}
        statement should not be abused.
        <br />
        <Code blocked colored>
          grid = [repeat 10: [repeat 10: 0]] \\creates a 10x10 grid of 0s
          <br />
          repeat: <br />
          &emsp; &emsp; if random([0...100)) as int == 0: break
        </Code>
        The most common use of the <Code>repeat</Code> loop is to initialize
        lists to a fixed size and for unconditional repetition. <br />
        The <Code>break</Code> statement will terminate the most recent loop.
        <br />
        <Code blocked colored>
          while (input = io read()) then input lowercase != "q": <br />
          &emsp; &emsp; io write(input)
        </Code>
        The most common use of the <Code>while</Code> loop is to await a
        condition.
        <br />
        Constants declared in the condition of the while loop will be accessible
        in the condition and the body of the loop for every pass of the loop.
        <br />
        <Code blocked colored>
          for i in [0...10): <br />
          &emsp; &emsp; io write("*" * 10) \\prints a 10x10 block of stars
        </Code>
        The for loop is the most commonly used loop. It can be used to repeat a
        block of code a fixed number of times, to use every value of an array,
        and to test every number of a range.
        <br />
        Constants declared in the condition of the for loop (before the{" "}
        <Code>in</Code> operator) will be accessible in the body of the loop for
        every pass of the loop.
        <br />
        <br />
      </div>
      <LargeText className="my-[10px]">Functions</LargeText>
      <div className="m-[10px]">
        Breaking the code into smaller pieces makes it easier to write and
        change the program. The following is an example of a function:
        <Code blocked colored>
          pow4(n) = n * n * n * n <br />
          io write(pow4(1)) \\1 <br />
          io write(pow4(2)) \\8 <br />
          io write(pow4(3)) \\81
        </Code>
        The basic structure is <Code>name(parameters) = code</Code>
        <Code blocked colored>
          piglatin(s = string) = &#123;return ret = string&#125; : &#123;
          <br />
          &emsp; &emsp; ret = var: s[[1 ... s length)]
          <br />
          &emsp; &emsp; ret += " "
          <br />
          &emsp; &emsp; ret += s[0] + "ay"
          <br />
          &#125;
          <br />
          io write(piglatin("hello")) \\ello hey <br />
          io write(piglatin("banana")) \\anana bay
        </Code>
        The function can be multiline. And if there are many lines of code,
        using a brace (<Code>&#123;&#125;</Code>) enclosed block with a{" "}
        <Code>return</Code>{" "}
        <Code blocked colored>
          sqrt(n = float) = &#123;positive, negative = int, int&#125; : &#123;
          <br />
          &emsp; &emsp; positive = n */ 2
          <br />
          &emsp; &emsp; negative = - n */ 2
          <br />
          &#125;
          <br />
          <br />
          &#123; a = positive, b = negative &#125; = root16 = sqrt(16) <br />
          <br />
          io write(a) \\4 <br />
          io write(root16 negative) \\-4
        </Code>
        This is a combination of the type-conversion and the direct
        function-value syntax. This makes it easy to return multiple values.{" "}
        <br />
        Destructuring isn't necessary, but it makes this relatively easy to use.
      </div>
    </section>
  );
}
