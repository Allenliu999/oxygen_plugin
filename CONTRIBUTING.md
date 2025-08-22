# Contributing to Oxygen Lock Plugin

Thank you for your interest in contributing to this project! Here are some guidelines to help you get started.

## Development Setup

1. **Prerequisites**
   - JDK 21+
   - Maven 3.6+
   - Oxygen XML Editor 26.x (for testing)

2. **Clone and Setup**
   ```bash
   git clone https://github.com/Allenliu999/oxygen-lock-plugin.git
   cd oxygen-lock-plugin
   mvn clean compile
   ```

3. **IDE Setup**
   - Import as Maven project
   - Ensure JDK 21 is configured
   - Add Oxygen SDK JARs to classpath if needed

## Building and Testing

```bash
# Build the project
mvn clean package

# Create plugin package
.\package_plugin.ps1

# Install in Oxygen for testing
# Use Help → Install new add-ons... and select dist/oxygen-plugin.zip
```

## Code Style

- Follow standard Java conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise

## Submitting Changes

1. Create a new branch for your feature/fix
2. Make your changes with clear commit messages
3. Test thoroughly with Oxygen XML Editor
4. Submit a pull request with description of changes

## Reporting Issues

When reporting bugs, please include:
- Oxygen XML Editor version
- JDK version
- Steps to reproduce
- Expected vs actual behavior
- Any error messages or logs