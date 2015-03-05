# Richardo "GNU/dad" Stalmanu

[ ![Codeship Status](https://codeship.com/projects/37a97950-a356-0132-f1db-76961affc56e/status?branch=master)](https://codeship.com/projects/66008)

> I'd just like to interject for a moment. What you're referring to as Linux,
> is in fact, GNU/Linux, or as I've recently taken to calling it, GNU plus
> Linux. Linux is not an operating system unto itself, but rather another free
> component of a fully functioning GNU system made useful by the GNU corelibs,
> shell utilities and vital system components comprising a full OS as defined
> by POSIX.
>
> Many computer users run a modified version of the GNU system every day,
> without realizing it. Through a peculiar turn of events, the version of GNU
> which is widely used today is often called Linux, and many of its users are
> not aware that it is basically the GNU system, developed by the GNU Project.
>
> There really is a Linux, and these people are using it, but it is just a part
> of the system they use. Linux is the kernel: the program in the system that
> allocates the machine's resources to the other programs that you run. The
> kernel is an essential part of an operating system, but useless by itself; it
> can only function in the context of a complete operating system. Linux is
> normally used in combination with the GNU operating system: the whole system
> is basically GNU with Linux added, or GNU/Linux. All the so-called Linux
> distributions are really distributions of GNU/Linux.

Stalmanu is a rather opinionated Slack bot. His hobbies include just
interjecting for a moment, and... well that's about it. This is mostly an
experiment in interfacing with Slack.io. Stalmanu uses
[Boot](http://boot-clj.com/), which will be assumed installed in the remainder
of the README.

You can compile Stalmanu into a runnable jar using

```bash
boot build
```

and then start him using

```bash
java -jar target/stalmanu-0.1.0.jar my-super-secret-token
```

where you supply the Slack bot token.
