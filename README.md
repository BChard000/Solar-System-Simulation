# Solar-System-Simulation
This Java application simulates the motion of planets in a solar system using Swing for rendering. The simulation uses Newton's law of universal gravitation to calculate the forces between celestial bodies and updates their positions accordingly.

# Features
- Accelerated simulation of planetary orbits around the sun.
- Scale representation of planets' distances from the sun using astronomical units (AU).
- Planets are drawn with orbits that reflect their actual motion through space.
- The simulation includes a simple user interface window with resizable capabilities.

# Classes
- 'Frame': Extends JFrame and sets up the main application window, including initialization of the SolarSystemPanel.
- 'SolarSystemPanel': A custom JPanel that manages the list of Planet objects and handles the rendering (drawing) of the planets and their orbits.
- 'Planet': Represents a celestial body with properties like position, velocity, mass, and orbit. Each planet calculates its gravitational attraction to other bodies and updates its position in space.

# Physics
- Gravitational forces are calculated based on the distance between planets and the sun.
- The application avoids singularities in calculations (division by zero) by imposing a minimum distance threshold.
- The 'Planet' class includes methods to draw each planet and its orbit, as well as to update its position over time according to the calculated forces.

# Running the Simulation
The application is initialized and made visible on the Event Dispatch Thread (EDT) using 'SwingUtilities.invokeLater', ensuring thread safety for Swing components. The simulation runs a continuous loop, triggered by a 'Timer', which repaints the 'SolarSystemPanel' at fixed intervals to animate the motion of the planets.
