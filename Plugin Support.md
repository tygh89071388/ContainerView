# Other Plugin Support

The plugin provides support for protection plugin, as you can decide who can view the containers in you area.
<br>If your desired protection plugin is not listed here, feel free to create an new issue.

### 1. Bentobox (BSkyblock)
Add **`CONTAINER_VIEW_PROTECTION`** to the `protection.flags` section in the language file of Bentobox:
```
protection:
  command-is-banned: Command is banned for visitors
  flags:
    CONTAINER_VIEW_PROTECTION:
      description: '&a Toggle who can view container.'
      name: '&a Container View'
```
