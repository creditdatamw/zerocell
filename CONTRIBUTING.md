# How to Contribute

We'd love to get contributions from you!

If you have any questions or run into any problems, please create
an issue here or email us: it [at] creditdatamw.com

## Workflow

The supported workflow for contributing is as follows:

1.  Fork the repo
1.  Make a feature or bugfix branch (use `git checkout -b "your-feature-or-fix"`)
1.  Make your cool new feature or bugfix on your branch
1.  Include tests for your change
1.  From your branch, make a pull request 
1.  The maintainers will review your change 
1.  Once your change has been reviewed and accepted, wait for your change to be merged into `master`
1.  Delete your feature branch

## Style

We use [FindBugs][findbugs], which uses static analysis to look for bugs 
in Java code, to catch common bugs and we generally follow a style guide 
similar to the [Google Java Style Guide][style] with exception on 
indentation (we use 4 spaces)

## Issues & Pull Requests

When creating an issue please explain the steps to reproduce
the issue or include a code snippet to illustrate the issue in more
detail. The maintainers will review all issues created and we'll do
our best to give feedback if necessary. However, we recommend that you send
a Pull Request with a fix if you can manage - contributions are always welcome

As outlined above, you must create a feature or bugfix branch and make a 
pull request from the branch which the maintainers will review.
You may be asked to improve some things in the pull request and once the
pull request is satisfactory it will be merged into the mainline.

Please see the following post on writing [good commit messages](https://chris.beams.io/posts/git-commit/)

## Code Review

The repository on GitHub is kept in sync with an internal repository at
Credit Data CRB Ltd. For the most part this process should be transparent 
but it may have some implications on how pull requests are merged in.

When you submit a pull request on GitHub, it will be reviewed
inside (and possibly outside of) Credit Data CRB Ltd, and once the changes are
approved, your commits will be brought into the internal system for additional
testing. Once the changes are merged internally, they will be pushed back to
GitHub.

[findbugs]: http://findbugs.sourceforge.net/
[style]: https://google.github.io/styleguide/javaguide.html