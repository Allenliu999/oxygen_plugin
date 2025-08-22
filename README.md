# Oxygen XML Editor Lock Plugin

An Oxygen XML Editor plugin that provides document locking/unlocking functionality, automatic version management, and Git integration for DocBook projects.

## Features

- **Document Lock/Unlock**: Toolbar buttons and dialog for managing document locks
- **Automatic Version Management**: Monitors topic changes and automatically increments map version numbers
- **Version Control Integration**: Built-in Git operations for unified commits
- **DocBook Support**: Specifically designed for DocBook workflows with version attributes in `<bookid><edition value="x.x"/></bookid>`

## Requirements

- JDK 21+
- Oxygen XML Editor 26.x
- Maven 3.6+

## Installation

### From Source

1. Clone this repository:
   ```bash
   git clone https://github.com/Allenliu999/oxygen-lock-plugin.git
   cd oxygen-lock-plugin
   ```

2. Build the plugin:
   ```bash
   mvn clean package
   ```

3. Package the plugin for Oxygen:
   ```powershell
   .\package_plugin.ps1
   ```

4. Install in Oxygen XML Editor:
   - Go to `Help` → `Install new add-ons...`
   - Select the generated `dist/oxygen-plugin.zip` file
   - Restart Oxygen XML Editor

### Manual Installation

1. Copy the built JAR file to your Oxygen installation directory: `frameworks/docbook/`
2. Configure Document Type Association in Oxygen to use the custom extension
3. Ensure all dependency JARs from the `lib` directory are accessible

## Development

### Project Structure

```
oxygen-lock-plugin/
├── src/main/java/com/example/oxygenplugin/
│   ├── git/           # Git integration components
│   ├── listener/      # Document change listeners
│   ├── lock/          # Lock/unlock functionality
│   ├── ui/            # User interface components
│   ├── version/       # Version management
│   └── MainExtensionBundle.java
├── src/main/resources/
│   ├── framework.xml  # Oxygen framework configuration
│   └── plugin.xml     # Plugin configuration
├── lib/               # Dependencies (Oxygen SDK and libraries)
└── pom.xml           # Maven configuration
```

### Building

```bash
# Compile and package
mvn clean package

# Create installable plugin
.\package_plugin.ps1
```

## Configuration

The plugin integrates with Oxygen's Document Type Association system. Version number attributes are managed in the DocBook format:

```xml
<bookid>
    <edition value="1.0"/>
</bookid>
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## References

- [Oxygen XML Editor SDK Documentation](https://www.oxygenxml.com/developer.html)
- [DocBook Documentation](https://docbook.org/)

