echo true

SET sToken=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

SET sProject="applet-code-entel-peru"
SET sUser=cratzerdigitalreef

github_changelog_generator --user %sUser% --project %sProject% --token %sToken% --release-branch main

pause